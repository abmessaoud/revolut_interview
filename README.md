# revolut_interview
This project was created to resolve the technical interview for revolut.

This API is made without any framework, and with only a few dependencies.
The endpoints currently available are:
  `````
  /ping -> to test connection
  /accounts -> GET -  Fetchs many accounts accepting filters by query.
                      Filters availables: status (active, deactive), balance (numeric value)
            -> POST - Creates a new account using the data received in the body.
                      Currently available: balance (numeric value)
  /accounts/{id} ->  GET   - Fetchs only one account, received in the URL parameter.
                             Does not accepts any other filter.
                 ->  PUT   - Updates the account given by the url parameter with the data received in the body.
                             Currently available: balance (numeric value)
                 -> DELETE - Soft deletes an account, given by the numeric value in the url parameter.
  
  /transfers -> GET  - Fetchs many transfers accepting filters by query.
                       Filters availables: status (successful, rollback), source (numeric value),
                       transaction (numeric value), destination (numeric value)
             -> POST - Generates a new transfer between the accounts given by the body parameters.
                       Currently available: source (numeric value), transaction (numeric value),
                       destination (numeric value)
  /transfers/{id} ->  GET   - Fetchs only one transfer, received in the URL parameter.
                              Does not accept any other filter.
                  -> DELETE - Rollback the transfer given by the url parameter.
                              The way to revert it, is by generating the opposite transfer and after that
                              soft deleting the transfer with status rollback.
