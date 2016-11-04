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
  service mongod --config /etc/mongod.conf --replSet rs0
else
  echo "-> OK";
fi

# Check whether Solr is running, otherwise start Solr
echo "3. Check Solr:"
UP=$(ps | grep "solr" | wc -l);
if [ "$UP" -ne 2 ];
then
  echo "-> Solr is down, start Solr";
  service solr start
else
  echo "-> OK";
fi

# Connect Mongo and Solr with mongo-connector
mongo-connector --auto-commit-interval=0 -m localhost.localdomain:27017 -d solr_doc_manager -t http://localhost:8983/solr/datactu -n datactu.articles -w /opt/datactu/logs/mongo-connector.log --unique-key=id &