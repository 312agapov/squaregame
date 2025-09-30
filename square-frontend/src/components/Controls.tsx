interface Props {
  size: number;
  onSizeChange: (n: number) => void;
  onNewGame: () => void;
}

export default function Controls({ size, onSizeChange, onNewGame }: Props) {
  return (
    <div style={{ marginBottom: 12 }}>
      <label>
        Board size:{" "}
        <input
          type="number"
          value={size}
          min={3}
          onChange={(e) => onSizeChange(parseInt(e.target.value))}
        />
      </label>
      <button onClick={onNewGame} style={{ marginLeft: 8 }}>
        New Game
      </button>
    </div>
  );
}