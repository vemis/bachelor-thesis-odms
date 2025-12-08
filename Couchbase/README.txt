Commands:

docker run -d --name db_couchbase \
  -p 8091-8096:8091-8096 \
  -p 11210-11211:11210-11211 \
  -v couchbase_data:/opt/couchbase/var \
  couchbase/server:enterprise