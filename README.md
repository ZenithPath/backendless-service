# backendless-service
This app containts functionality related to login/registration and fetching history of all logins of all users. 
This asignment was given to me as a test task after an interview. Essentialy it was done in a day, so be aware of following drawbacks:
- asynchroneus events handling quite primitive (e.g. user can be registered only after his profile image was successfuly uploaded)
- requests and responses aren't cached, so simple rotate will terminate all processes
- written without Dagger and any manual presenter's caching
- some minor flaws (like allowing to upload an image of arbitrary size, linking profile image to the part of email before @ symbol etc)
