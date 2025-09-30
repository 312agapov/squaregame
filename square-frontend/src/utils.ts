export function createEmptyBoard(size: number): string[][] {
  return Array.from({ length: size }, () => Array(size).fill(" "));
}