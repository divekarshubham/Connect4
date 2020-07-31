# Connect Four Game

This game was designed as an assignment of Production Quality Software, Spring 20.

1. Design Patterns used in the system:

   * Builder Pattern for building a `ConnectFourModel`
   * Factory Pattern for initializing `Player` s from the `PlayerFactory`
   * Observer and Model-View-Controller pattern for communicating the results in the `ConnectFourModel` with the `ComputerPlayer` and `ConnectFourView`

2. Animations:

   * Falling action of coin

   * Highlightling(making tokens bigger for the winner and gray for the other) of the winner after game ends

   * Color change on mouse hover for the insert buttons

   * ![Screenshot (3)](/images/UI1.png)

     ![Screenshot (4)](/images/UI2.png)

3. AI modes:

4. - Easy: One step lookahead
   - Medium: Minimax with depth 6 with A/B pruning
   - Hard: Minimax with depth 10 with A/B pruning
   - Minimax algorithm used:
   - ![minimax](/images/minimax.png)

