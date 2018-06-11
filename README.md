Market Simulator
============================

run.sh - This script will let you run the application. Assuming Java and Maven is already installed.

Input
- The system will read inbound order information to help it build the order book for the stock.
- The information will be entered manually into the command line in the format [Side] [Quantity] [Price]
 

Valid values
- Side: Single character {B,S}, B for a buy order, S for a sell order
- Quantity: any value greater than 0
- Price: any value greater than zero to 3 decimal places
- Orders entered with invalid values should be rejected with a meaningful explanation.
 