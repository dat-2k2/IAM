# Swapiness warning
[Why](https://unix.stackexchange.com/questions/691795/what-is-the-different-between-settings-swappiness-to-0-to-swapoff)
# Create root user
1. Disable auth. In _/etc/mongod.conf_ set:
   ```yaml
   security:
    authorization: disabled
   ```
2. Restart _mongod.service_
3. Switch to database _admin_:
    ```jshelllanguage
    db.createUser({
        user: "root",
        pwd: passwordPrompt(),
        roles: [
            { role: "root", db: "admin" }
        ]
    })
    ```
4. Enable auth.
   In _/etc/mongod.conf_ set:
   ```yaml
   security:
    authorization: disabled
   ```
5. Restart mongod.service
# Create admin for database
In _admin_ database:
```jshelllanguage
db.createUser({
   user: "trip_planner_admin",
   pwd: passwordPrompt(),
   roles: [
      { role: "dbOwner", db: "trip_planner" }
   ]
})
```

