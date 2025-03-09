# Install jar app with env
```
docker build --tag iam:latest --build-arg JAR_PATH=PATH_TO_JAR --build-arg RESOURCE_PATH=PATH_TO_RESOURCE .
docker run -d --name=iam --env-file=PATH_TO_ENV -p 8888:8888 iam:latest
``` 
where all paths are **relative** to Dockerfile.

Add ```--network=host``` to ```docker run``` if need to use service running on host.
