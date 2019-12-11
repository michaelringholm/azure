# exit when any command fails
set -e

echo Installing data-transport-layer
cd data-transport-layer
mvn install
cd ..

echo Installing data-access-layer
cd data-access-layer
mvn install
cd ..

echo Installing service-layer
cd service-layer
mvn install
cd ..

echo Compiling business-layer
cd business-layer
mvn compile
cd ..

echo Installing paypal-service
cd paypal-service
mvn install
cd ..

echo Installing open-erp-service
cd open-erp-service
mvn install
cd ..

echo Installing docuware-service
cd docuware-service
mvn install
cd ..

echo Compiling az-blob-storage-layer
cd az-blob-storage-layer
mvn compile
cd ..

echo Compiling az-key-vault
cd az-key-vault
mvn compile
cd ..

echo Compiling az-sql-server-dal
cd az-sql-server-dal
mvn compile
cd ..

echo Compiling az-blob-storage-layer-test
cd az-blob-storage-layer
mvn test
cd ..