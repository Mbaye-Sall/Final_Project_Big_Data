### How to build the package
- sbt compile
- sbt clean package
- mkdir jars
- cp target/scala-2.12/sparkretails_2.12-0.1.jar jars/

### How to run the Example

#### Standalone job submission
* spark-submit --class com.retails.MainApplication sparkretails_2.12-0.1.jar

####  job submission in yarn cluster
* spark-submit --class com.retails.MainApplication \
    --master yarn \
    --deploy-mode cluster \
    --driver-memory 4g \
    --executor-memory 2g \
    --executor-cores 1 \
    --queue thequeue \
    sparkretails_2.12-0.1.jar

