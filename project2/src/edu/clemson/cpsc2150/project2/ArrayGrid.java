package edu.clemson.cpsc2150.project2;

/**
 * Created by andrewmarionhunter on 9/21/16.
 */
class ArrayGrid implements Grid {

    private int myRowCount, myColCount;
    private Status[][] myStatusGrid;
    private int[][] myShipGrid;
    private int[] myShipHitsRemaining;
    private int myTotalHitsRemaining;

    private int myShipToBePlaced;
    private int myLastSunkShip;


    public void setGridDimensions(int rows, int cols)
    {
        myRowCount = rows;
        myColCount = cols;

        myStatusGrid = new Status[myRowCount][myColCount];
        myShipGrid = new int[myRowCount][myColCount];
        myShipHitsRemaining = new int[DEFAULT_SHIP_COUNT];
        myTotalHitsRemaining = 0;
    }

    public boolean isConflictingShipPlacement (Ship ship) {
        // loop through each coordinate of the ship to be placed
        for (int i = 0; i < ship.getLength(); ++i) {


            // does the ship go off the grid?
            if (ship.getCoordinates()[i].row < 0 || ship.getCoordinates()[i].col < 0 || ship.getCoordinates()[i].row >= myRowCount || ship.getCoordinates()[i].col >= myColCount) {
                return true;
            }

            // does the ship overlap with another ship?
            if (myStatusGrid[ship.getCoordinates()[i].row][ship.getCoordinates()[i].col] == Status.SHIP) {
                return true;
            }
        }
        return false;
    }

    public void placeShip(Ship ship)
    {
        // set the remaining number of hits for this ship
        myShipHitsRemaining[myShipToBePlaced] = ship.getLength();
        myTotalHitsRemaining += ship.getLength();

        // loop through each coordinate of the ship to be placed
        for (int i = 0; i < ship.getLength(); ++i) {
            // set the status and ship at current coordinates
            myStatusGrid[ship.getCoordinates()[0].row][ship.getCoordinates()[0].col] = Status.SHIP;
            myShipGrid[ship.getCoordinates()[0].row][ship.getCoordinates()[0].col] = myShipToBePlaced;

            // increment the ship coordinates
            if (ship.getDirection() == Direction.DOWN) {
                ++ship.getCoordinates()[0].row;
            } else {
                ++ship.getCoordinates()[0].col;
            }
        }
    }

    // see Ship.shoot()
    public Status shoot(Coordinate coord)
    {
        // is it a hit?
        if (myStatusGrid[coord.row][coord.col] == Status.SHIP) {
            // change status to hit
            myStatusGrid[coord.row][coord.col] = Status.HIT;

            // decrement the remaining number of hits
            int ship = myShipGrid[coord.row][coord.col];
            --myShipHitsRemaining[ship];
            --myTotalHitsRemaining;

            // is the ship sunk?
            if (myShipHitsRemaining[ship] == 0) {
                // the ship is sunk!
                myLastSunkShip = ship;
                return Status.SUNK;
            } else {
                // the ship is hit, but not sunk
                return Status.HIT;
            }
        } else {
            // change status to miss
            myStatusGrid[coord.row][coord.col] = Status.MISS;
            return Status.MISS;
        }
    }

    // returns Ship object representing the last ship
    // which was sunk(null if no ship has been sunk)
    public Ship getLastSunkShip()
    {
        ShipImpl potato = new ShipImpl(ShipType.CARRIER);
        Coordinate x = new Coordinate();
        x.row = 3;
        x.col = 4;

        return potato;
    }

    public boolean hasBeenAttempted(Coordinate coord)
    {
        return (myStatusGrid[coord.row][coord.col] == Status.MISS || myStatusGrid[coord.row][coord.col] == Status.HIT);

    }

    public void displayGrid(boolean showShips)
    {
        System.out.println();
        for (int i = -1; i < myRowCount; ++i) {
            if (i == -1) {
                System.out.print("   ");
                for (int j = 0; j < myColCount; ++j) {
                    System.out.printf("%4d", j);
                }
                System.out.println();
            } else {
                System.out.print("\n  " + i);
                for (int j = 0; j < myColCount; ++j) {
                    System.out.printf("%4s", getGridChar(i, j, showShips));
                }
                System.out.println();
            }
        }
    }

    private String getGridChar(int row, int col, boolean showShips) {
       if   (myStatusGrid[row][col] == Status.MISS)
        {
            return "+";
        }
       else if(myStatusGrid[row][col] == Status.HIT)
       {
           return "X";
       }
       else if (myStatusGrid[row][col] == Status.SHIP)
       {
           if (showShips) {
               return "@";
           } else {
               return "-";
           }
       }
       else
       {
           return "-";
       }






        /*switch(myStatusGrid[row][col]) {
            case MISS:
                return "+";
            case HIT:
                return "X";
            case SHIP:
                if (showShips) {
                    return "@";
                } else {
                    return "-";
                }
            default:
                return "-";
        }*/
    }

    // returns true if all ships have been sunk
    // otherwise, false
    public boolean isGameOver()
    {
        return (myTotalHitsRemaining == 0);
    }
}
