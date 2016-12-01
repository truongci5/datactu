#!/bin/bash

# Move to the directory where there is the executable jar

# Check whether MySQL is running, otherwise start MySQL
echo "1. Check MySQL:"
UP=$(pgrep mysql | wc -l);
if [ "$UP" -ne 1 ];
then
  echo "-> MySQL is down, start MySQL";
  service mysqld start
else
  echo "-> OK";
fi

# Check whether MongoDB is running, otherwise start MongoDB
echo "2. Check MongoDB:"
UP=$(pgrep mongod | wc -l);
if [ "$UP" -ne 1 ];
then
  echo "-> MongoDB is down, start MongoDB";
  mongod --config /etc/mongod.conf --replSet rs0 &
else
  echo "-> OK";
fi

# Check whether Solr is running, otherwise start Solr
echo "3. Check Solr:"
UP=$(curl -s -o /dev/null -I -w '%{http_code}' http://localhost:8983/solr/admin/cores?action=STATUS);
if [ "$UP" -ne 200 ];
then
  echo "-> Solr is down, start Solr";
  /opt/solr/bin solr restart
else
  echo "-> OK";
fi

# Check whether Mongo-connetor is running, otherwise connect Mongo and Solr with mongo-connector
echo "4. Check Mongo-connector:"
UP=$(pgrep mongo-connector | wc -l);
if [ "$UP" -ne 1 ];
then
  echo "-> Mongo-connector is down, start mongo-connector";
  mongo-connector --auto-commit-interval=0 -m localhost.localdomain:27017 -d solr_doc_manager -t http://localhost:8983/solr/datactu -n datactu.articles -w /opt/datactu/logs/mongo-connector.log --unique-key=id &
else
  echo "-> OK";
fi