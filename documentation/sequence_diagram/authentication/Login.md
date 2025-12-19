```
@startuml
title Authentication - Login

actor User
entity Front_End
entity AuthController
entity UserService
database Database
entity JwtUtils

group Login Process
    User -> Front_End: input username & password
    Front_End -> AuthController: POST /api/auth/login\n(LoginRequest)
    activate AuthController

    AuthController -> UserService: login(username, password)
    activate UserService

    UserService -> Database: findByUsername(username)
    activate Database
    Database --> UserService: User data
    deactivate Database

    UserService -> UserService: passwordEncoder.matches()
    
    alt Password valid
        UserService -> JwtUtils: generateToken(username)
        JwtUtils --> UserService: JWT Token
        UserService --> AuthController: LoginResponse\n(token, user data)
    else Password invalid
        UserService --> AuthController: RuntimeException
    end

    deactivate UserService
    AuthController --> Front_End: HTTP 200 OK\n(LoginResponse)
    deactivate AuthController

    Front_End --> User: login success (store token)
end
@enduml
```