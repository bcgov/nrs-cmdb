rem @echo off
echo BASE URL is %1
SET server=%1

echo
echo SETTING A RANDOM VALUE...
echo
curl -v --data "project=ARTS&environment=TEST&component=arts-client-war&property=random&value=%RANDOM%" %server%/properties/value

echo
echo GETTING A VALUE...
echo
curl -v "%server%/properties/value?project=ARTS&environment=TEST&component=arts-client-war&property=random"

echo
echo SECOND GET...
echo

curl -v "%server%/properties/value?project=ARTS&environment=TEST&component=arts-client-war&property=random"

echo
echo SET VALUE TO A NEW RANDOM...
echo
curl --data "project=ARTS&environment=TEST&component=arts-client-war&property=random&value=%RANDOM%" %server%/properties/value

echo
echo GETTING VALUE...
echo
curl "%server%/properties/value?project=ARTS&environment=TEST&component=arts-client-war&property=random"
