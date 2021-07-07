public class SnakePiece {
    int x, y;
    public SnakePiece(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    boolean equals(SnakePiece piece)
    {
        if(x == piece.x&&y == piece.y)
        {
            return true;
        }
        return  false;
    }

}
