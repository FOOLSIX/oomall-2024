#!/bin/bash
# recursive upload files
function traverse() {
    for file in $(ls $1)
    do
        if [ -d "$1/$file" ]; then
          curl -X MKCOL $2/$file/ -u mingqiu:$3
          traverse "$1/$file" "$2/$file" "$3"
        else
          #echo "curl -T $file $2/$file -u mingqiu:$3"
          curl -T $1/$file $2/$file -u mingqiu:$3
        fi
    done
}

## 将文件结尾从CRLF改为LF，解决了cd 错误问题
TIME=$(TZ=UTC-8 date "+%Y-%m-%d-%H-%M-%S")

cd /OOMALL
git pull

echo “copy pom file.....”
cp pom.bak.xml pom.xml
echo “core testing.....”
mvn clean install > core.log
mvn jacoco:report

curl -X MKCOL  $1/test/unit-test/$TIME/ -u mingqiu:$2

#docker exec redis redis-cli -a 123456 flushdb

cd core
curl -X MKCOL  $1/test/unit-test/$TIME/core/ -u mingqiu:$2
curl -X MKCOL  $1/test/unit-test/$TIME/core/jacoco/ -u mingqiu:$2
curl -X MKCOL  $1/test/unit-test/$TIME/core/test-result/ -u mingqiu:$2
traverse "/OOMALL/site/images"  "$1/test/unit-test/$TIME/core/test-result/images" "$2"
traverse "/OOMALL/site/css"  "$1/test/unit-test/$TIME/core/test-result/css" "$2"
traverse "target/site/jacoco" "$1/test/unit-test/$TIME/core/jacoco" "$2"
curl -T target/site/surefire.html $1/test/unit-test/$TIME/core/test-result/index.html -u mingqiu:$2
curl -T ../core.log $1/test/unit-test/$TIME/core/core.log -u mingqiu:$2

cd /OOMALL
git checkout -- pom.xml

module=("payment" "region" "product" "shop" "sfexpress" "wechatpay" "jtexpress" "alipay" "freight" "ztoexpress")
length=${#module[@]}
#echo "length =" $length
for ((i=0;i<$length;i++))
do
  #index=$(($RANDOM%$length))
  #echo "index =" $index
  M=${module[$i]}
  echo $M "testing............"
  cd /OOMALL/$M
  mvn clean site > $M.log
  mvn jacoco:report
  #echo "curl -X MKCOL  $1/test/unit-test/$TIME/$M/ -u mingqiu:$2"
  curl -X MKCOL  $1/test/unit-test/$TIME/$M/ -u mingqiu:$2
  curl -X MKCOL  $1/test/unit-test/$TIME/$M/jacoco/ -u mingqiu:$2
  curl -X MKCOL  $1/test/unit-test/$TIME/$M/test-result/ -u mingqiu:$2
  traverse "target/site/jacoco" "$1/test/unit-test/$TIME/$M/jacoco" "$2"
  curl -T target/site/surefire.html $1/test/unit-test/$TIME/$M/test-result/index.html -u mingqiu:$2
  curl -T $M.log $1/test/unit-test/$TIME/$M/$M.log -u mingqiu:$2
  traverse "/OOMALL/site/images"  "$1/test/unit-test/$TIME/$M/test-result/images" "$2"
  traverse "/OOMALL/site/css"  "$1/test/unit-test/$TIME/$M/test-result/css" "$2"
done

