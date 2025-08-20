## How to get code:

>> https://drive.google.com/file/u/1/d/1jnXtX3cUCS1ZLloWEatTZzFkDt2qBJAz/view?usp=drive_link&pli=1

## How to run

Open evnvironment -> Run
> docker-compose -f environment/docker-compose-dev.yml up

The above cmd will auto create db with these metrics:
```bash
MYSQL_ROOT_PASSWORD: root1234
MYSQL_DATABSE: ticket
MYSQL_PASSWORD: root1234 
```

Caution: 
    - When run sucessfully will create a folder `data/db_data` in `environment` \
    - if data exist -> please remove before run

## How to test
