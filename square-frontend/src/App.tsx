import { useState } from "react";
import "./styles.css";
import Board from "./components/Board";
import Controls from "./components/Controls";
import { createEmptyBoard } from "./utils";
import { startGame, makeMove, exitGame, nextMove } from "./api";
import { type MoveResultDto, type BoardDto } from "./interfaces";

export default function App() {
  const [size, setSize] = useState<number>(5);
  const [board, setBoard] = useState(createEmptyBoard(size));
  const [userColor, setUserColor] = useState<"w" | "b">("w");
  const [status, setStatus] = useState<string>("");
  const [gameStarted, setGameStarted] = useState<boolean>(false);

  async function handleNewGame() {
    if (isNaN(size) || size < 3) {
      setStatus("Size must be an integer >= 3");
      return;
    }

    let p1 = "user " + userColor;
    let p2 = "";
    if (userColor === "w") {
      p2 = "comp b";
    } else {
      p2 = "comp w";
    }

    setStatus("Starting game...");
    const msg = await startGame(size, p1, p2);
    setBoard(createEmptyBoard(size));
    setStatus(msg);

    if (msg && msg.toLowerCase().indexOf("new game started") !== -1) {
      setGameStarted(true);
    } else {
      setGameStarted(false);
    }
  }

  async function handleCellClick(x: number, y: number) {
    if (!gameStarted) {
      setStatus("Start a game first");
      return;
    }

    setStatus("Sending move to server...");
    const resp: MoveResultDto | null = await makeMove(x, y);

    if (resp === null) {
      setStatus("Server error or invalid response");
      return;
    }

    if (!resp.valid) {
      setStatus("Incorrect move");
      return;
    }

    // 1) Применяем ход пользователя (в любом случае, если valid == true)
    setBoard((prev) => {
      const copy = prev.map((row) => row.slice());
      if (y >= 0 && y < copy.length && x >= 0 && x < copy[0].length) {
        copy[y][x] = userColor;
      }
      return copy;
    });

    // 2) Обрабатываем ход сервера (обычно — компьютера)
    const serverX = resp.x;
    const serverY = resp.y;

    let compMoved = false;

    if (typeof serverX === "number" && typeof serverY === "number") {
      if (serverX >= 0 && serverY >= 0 && serverX < size && serverY < size) {
        if (!(serverX === x && serverY === y)) {
          let compColor: "w" | "b";
          if (userColor === "w") {
            compColor = "b";
          } else {
            compColor = "w";
          }

          setBoard((prev) => {
            const copy = prev.map((row) => row.slice());
            copy[serverY][serverX] = compColor;
            return copy;
          });

          compMoved = true;
          setStatus("Computer moved at (" + serverX + ", " + serverY + ")");
        } else {
          setStatus("Your move accepted");
        }
      } else {
        setStatus("Server returned out-of-range coordinates");
      }
    } else {
      setStatus("Server did not return coordinates");
    }

    // 3) Проверяем окончание игры
    if (resp.gameOver) {
      if (resp.draw) {
        setStatus("Game finished: Draw");
      } else {
        if (resp.winner === "WHITE") {
          setStatus("Game finished: White wins!");
        } else if (resp.winner === "BLACK") {
          setStatus("Game finished: Black wins!");
        } else {
          setStatus("Game finished");
        }
      }
      setGameStarted(false);
      return;
    }

    if (!resp.gameOver) {
      if (!compMoved) {
        setStatus("Move accepted");
      }
    }
  }

  async function handleExit() {
    await exitGame();
    setBoard(createEmptyBoard(size));
    setStatus("Game exited");
    setGameStarted(false);
  }

  // обработчик подсказки
  async function handleHelp() {
    if (!gameStarted) {
      setStatus("Start a game first");
      return;
    }

    const data = board.map((row) => row.join("")).join("");

    const dto: BoardDto = {
      size: size,
      data: data,
      nextPlayerColor: userColor,
    };

    const resp = await nextMove("rules", dto); // "rules" по факту не используется
    if (resp === null) {
      setStatus("Help request failed");
      return;
    }

    setStatus(`Help: suggested move (${resp.x}, ${resp.y}), color ${resp.color}`);
  }

  return (
    <div style={{ padding: 20 }}>
      <h2>Squares Game</h2>

      <Controls size={size} onSizeChange={setSize} onNewGame={handleNewGame} />

      <div style={{ marginBottom: 12 }}>
        <label>
          You are:
          <select
            value={userColor}
            onChange={(e) => {
              const v = e.target.value;
              if (v === "w" || v === "b") {
                setUserColor(v as "w" | "b");
              }
            }}
          >
            <option value="w">White (W)</option>
            <option value="b">Black (B)</option>
          </select>
        </label>
      </div>

      <Board board={board} onCellClick={handleCellClick} />

      <div style={{ marginTop: 16 }}>{status}</div>

      <div style={{ marginTop: 10, display: "flex", gap: 10 }}>
        <button onClick={handleExit}>Exit Game</button>
        <button onClick={handleHelp}>Help</button>
      </div>
    </div>
  );
}
