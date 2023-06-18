# AWS S3 -  Spring Boot Example



### Technologies:

 - Java 17
 - Maven
 - Docker / Docker Compose
 - Localstack

### API Features

- Create bucket
- Delete bucket
- Get Images from bucket
- Put Images `.png or .jpg`
- Delete Images


### Run Application:

1. ```sh
   docker-compose  up -d
   ```
2. ```shell
   ./mvnw spring-boot:run
   ```

### Requests:

- Create bucket:
   ```curl
   curl --location --request POST 'http://localhost:8080/s3/bucket?bucketName=images'
   ```

- Delete bucket:
  ```shell
  curl --location --request DELETE 'http://localhost:8080/s3/bucket?bucketName=images'
  ```
  
- Get objects:
   ```curl
   curl --location 'http://localhost:8080/s3/image?bucketName=images'
   ```

- Put Object:
  ```shell
  curl --location 'http://localhost:8080/s3/image?bucketName=images' \
  --form 'file=@"{YOUR_FILE_PATH}"'
  ```
   - Replace `{YOUR_FILE_PATH}` for example `/home/me/image.png`


- Delete Object:
  ```shell
  curl --location --request DELETE 'http://localhost:8080/s3/image?bucketName=images&fileName={YOUR_FILE_NAME}'
  ```
  - Replace `{YOUR_FILE_NAME}` for example `image.png`

