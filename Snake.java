/**
 * Edward Davis
 * This snake class is its own separate class from the Game Manager because it
 * has much of its own bookkeeping, and this made it easier to manage.
 *
 */

import java.util.LinkedList;
import java.awt.Point;

 class Snake implements SnakeInterface{
    boolean growth = false;
    Point head;
    LinkedList<Point> body;

    /**
     * Constructor class for a snake. The head is a point containing the
     * coordinates for the snake's head, and the body is a list of points
     * representing the snake's body.
     * @param start The beginning location for the snake's head
     */
    Snake(Point start) {
        head = start;
        body = new LinkedList<Point>();
        grow();
    }

    /**
     * Helper method to activate growth by setting the variable to true
     */
    void grow() {
        growth = true;
    }

     /**
      * Helper method to remove the last Point in the body list, allowing the
      * snake's body to remain a constant size or shrink if the snake eats
      * the poison food.
      */
    void shrink(){
        body.removeLast();
    }

    /**
     * Method to make the snake move. The direction is passed as an enum. The
     * position of the head is added to the list representing the body, and
     * the head has one of its coordinates incremented or decremented so that
     * it moves up, down, left, or right on the map, and it's old position
     * becomes occupied by the body. The last position is deleted during a
     * regular movement such that the number of positions in the snake's body
     * remains constant. If the snake is growing, the last position is left
     * without being deleted, so the number of positions in the body grows by
     * one when the head's position is added.
     * @param direction The enum type which specifies the direction the snake
     *                 moves
     */
    public void move(Direction direction) {
        body.addFirst(new Point(head.x,head.y));

        if (!growth) {shrink();}
        growth = false;

        switch (direction){
            case UP:
                head.y--;
                break;
            case DOWN:
                head.y++;
                break;
            case LEFT:
                head.x--;
                break;
            case RIGHT:
                head.x++;
                break;
        }
    }


}
