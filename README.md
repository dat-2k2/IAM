# Install jar app with env
```
docker build --tag iam:latest --build-arg JAR_PATH=PATH_TO_JAR_FILE --build-arg CONFIG_PATH=PATH_TO_CONFIG_FILE.
docker run -d --name=iam --env-file=PATH_TO_ENV -p 8888:8888 iam:latest
``` 
where all paths are **relative** to Dockerfile.

Add ```--network=host``` to ```docker run``` if need to use service running on host.
