import type { MoveResultDto, SimpleMoveDto, BoardDto } from "./interfaces";

const BASE = "/api"; // используем относительный путь, Vite proxy направит на бэк

export async function startGame(size: number, player1: string, player2: string): Promise<string> {
  try {
    const url = `${BASE}/game?size=${size}&player1=${encodeURIComponent(player1)}&player2=${encodeURIComponent(player2)}`;
    const resp = await fetch(url, { method: "POST" });
    const text = await resp.text();
    if (!resp.ok) {
      console.error("startGame failed:", resp.status, text);
      return text;
    }
    return text;
  } catch (err) {
    console.error("startGame network error:", err);
    return "Network error";
  }
}

export async function makeMove(x: number, y: number): Promise<MoveResultDto | null> {
  try {
    const url = `${BASE}/move?x=${x}&y=${y}`;
    const resp = await fetch(url, { method: "POST" });
    if (!resp.ok) {
      const text = await resp.text();
      console.error("makeMove failed:", resp.status, text);
      return null;
    }
    const json = await resp.json();
    return json as MoveResultDto;
  } catch (err) {
    console.error("makeMove network error:", err);
    return null;
  }
}

export async function exitGame(): Promise<void> {
  try {
    await fetch(`${BASE}/exit`, { method: "POST" });
  } catch (err) {
    console.error("exitGame network error:", err);
  }
}

// 🔹 новый метод для эндпоинта /{rules}/nextMove
export async function nextMove(rules: string, boardDto: BoardDto): Promise<SimpleMoveDto | null> {
  try {
    const resp = await fetch(`${BASE}/${rules}/nextMove`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(boardDto),
    });
    if (!resp.ok) {
      const text = await resp.text();
      console.error("nextMove failed:", resp.status, text);
      return null;
    }
    const json = await resp.json();
    return json as SimpleMoveDto;
  } catch (err) {
    console.error("nextMove network error:", err);
    return null;
  }
}
