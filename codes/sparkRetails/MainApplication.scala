package com.retails

import org.apache.spark.sql.SparkSession

object MainApplication extends  App {

  val spark = SparkSession.builder
    .master("local[*]")
    .appName("Retail App")
    .getOrCreate()

  val orders = spark.read
    .schema("""order_id INT, order_date TIMESTAMP,
              order_customer_id INT, order_status STRING
           """)
    .format("csv")
    .load("/home/dmboup/gitProjects/apache_spark_data_processing/datasets/retail_db/orders")

  val order_items = spark.
    read.
    schema("""order_item_id INT, order_item_order_id INT, order_item_product_id INT,
                  order_item_quantity INT, order_item_subtotal DOUBLE, order_item_product_price DOUBLE""").
    option("sep", ",").csv("/home/dmboup/gitProjects/apache_spark_data_processing/datasets/retail_db/order_items")

  val customers = spark.
    read.
    schema("""customer_id INT, customer_fname STRING, customer_lname STRING,
                customer_email STRING, customer_password STRING, customer_street STRING,
                customer_city STRING, customer_state STRING, customer_zipcode STRING""").
    option("sep", ",").
    csv("/home/dmboup/gitProjects/apache_spark_data_processing/datasets/retail_db/customers")

  orders.createOrReplaceTempView("orders")
  customers.createOrReplaceTempView("customers")
  order_items.createOrReplaceTempView("order_items")

  // Liste de tous les clients qui ont passé une commande d'un montant supérieur à 200 $

  val listeClients = spark.sql("SELECT customer_fname as fname, customer_lname as lname, customer_city as city, order_amount " +
    "FROM (SELECT order_item_order_id, sum(order_item_subtotal) as order_amount  FROM order_items  GROUP BY " +
    "order_item_order_id HAVING sum(order_item_subtotal) > 200) " +
    "JOIN orders ON order_id = order_item_order_id JOIN customers ON order_customer_id = customer_id " +
    "ORDER BY order_amount DESC")
  listeClients.show()


  listeClients.
    coalesce(1).
    write.
    mode("overwrite").
    option("compression", "snappy").
    format("parquet").
    save("/home/dmboup/gitProjects/apache_spark_data_processing/datasets/output/orders")
}
