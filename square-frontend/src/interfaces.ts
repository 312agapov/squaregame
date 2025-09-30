export interface BoardDto {
  size: number;
  data: string;           // строка length = size*size, ' ' пусто, 'w'/'b' фишки
  nextPlayerColor: string; // 'w' or 'b'
}

export interface MoveResultDto {
  valid: boolean;
  x: number;
  y: number;
  gameOver: boolean;
  winner: "WHITE" | "BLACK" | null;
  draw: boolean;
}

export interface SimpleMoveDto {
  x: number;
  y: number;
  color: string; // 'w' or 'b' — цвет, который сходил
}