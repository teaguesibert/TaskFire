# JSON object definitions

All fields in this documentation are required unless marked optional.

The server will not throw an error when deserializing based on an unexpected ordering of JSON fields, the client should
ensure that all fields are present unless marked optional.

The server does not guarantee that the keys will appear in a particular order when serializing JSON content.

I.E. The server is agnostic to the order the JSON keys are received and sent.

## Account

An account object.

You can send a `GET` request to  `/accounts` with a body of `{ "name": "the user name" }` to obtain more information
about an account (E.G. the account ID). However, the server will always return an empty string for the `password` field.
It is recommended the client only include the `password` field when sending `GET` requests to `/auth` and omit it in all
other instances.

### Fields

* String `name`: The username of the account.
* String `password`: (Optional) The account's password. While this field is not required for the
  server to understand the data, it is required to be set and not empty when making a `POST` request to `/register`. It
  is recommended
  that this be encrypted by the client. The only validation check performed is that the password is not empty,
  the client is resposible for enforcing good password conventions.
* String `id`: (Optional) The account's universally unique identifier (UUID). The server will
  generate this value for
  new accounts. The client may omit this when creating new accounts.

## Auth Token

A token used to ensure the request is valid and authorized to make changes to account information and data owned by an
account.

For now, sessions are maintained for the lifetime of the application and are invalided when a new session for a user
begins. This is very likely to change in the future. In short, all `GET` requests to `/auth` result in a new Auth Token
and all previous tokens are invalid.

### Fields

* String `sessionId`: The UUID of the current client's session.
* String `accountId`: The UUID of the account.
* Integer `timeStamp`: The timestamp when token was created represented by the server's time in milliseconds since
  EPOCH.

## Task

Task data belonging to an account. An account may have many tasks.

### Fields

* String `title`: The title of the task.
* String `description`: (Optional) The description of the task.
* String `taskId`: (Optional) The task's UUID. The client may omit this field when POSTing a new task.
* String `accountId`: The UUID of the task's owner account.
* Boolean `completed`: Whether the task is completed; true when completed.
* Integer `created`: The time in milliseconds when the task was created. The client should provide this value. If the
  value is less than or equal to 0, then the server will assign its system time that it received the request as the
  task's created value.
* Integer `modified`: (Optional) The time in milliseconds when the task was last modified. The server will provide this
  value, ignoring what the client provides, if the client chooses to populate this field.

## TaskWrapper

Wraps `AuthToken` and `Task` objects so that the server can determine the request is valid and authorized to alter
account's task data.

### Fields

* AuthToken `authToken`: The auth token of the session.
* Task `task`: The task being created or modified.

# Routes

## Account

The following routes are those related to the accounts.

### GET /accounts

Responds with a list of `Account`s if the request is valid. Or just an error status if something went wrong.

#### Error codes

* `415` When the request body is missing expected data.
* `401` When the request cannot be authenticated. This may be due to an expired auth token or insufficient permissions.

#### Query parameters

* `name` (Optional) The account name to query for.
  When included, the response will be a singleton list with the matching Account object or empty if there is no match.
  When omitted all accounts that client has permissions to view will be included in the response.

#### Body

JSON, the valid `AuthToken` of the session mapped to the account, which can be acquired from the `GET /auth`
route.

### GET /auth

Responds with a `AuthToken` mapped to the account matching both name and password fields from the body.

#### Body

JSON, `Account` with name and password fields set.

### GET /register

Creates a new account with the server.

#### Error codes

* 409 An account with the given name already exists

#### Body

JSON, `Account` with name and password fields set.

## Task

The following routes are those related to tasks.

### GET /tasks/{accountId}

Responds with a list of `Task`s.

#### Error codes
* `401` If the auth token is invalid or if the token lacks permission to request this resource.

#### Body
JSON, `AuthToken`.

### POST /tasks/{accountId}

Updates or creates a new `Task`.

#### Error codes
* `401` If the auth token is invalid or if the token lacks permission to request this resource.

#### Body
JSON, `TaskWrapper`.