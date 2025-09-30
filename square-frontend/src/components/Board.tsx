interface Props {
  board: string[][];
  onCellClick: (x: number, y: number) => void;
}

export default function Board({ board, onCellClick }: Props) {
  return (
    <div style={{ display: "inline-block", marginTop: 16 }}>
      {board.map((row, y) => (
        <div key={y} style={{ display: "flex" }}>
          {row.map((cell, x) => (
            <div
              key={x}
              onClick={() => onCellClick(x, y)}
              style={{
                width: 40,
                height: 40,
                border: "1px solid black",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                cursor: "pointer",
                fontWeight: "bold",
                fontSize: 20,
              }}
            >
              {cell === "w" ? "W" : cell === "b" ? "B" : ""}
            </div>
          ))}
        </div>
      ))}
    </div>
  );
}