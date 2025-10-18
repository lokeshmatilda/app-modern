app list details:
----------------
chat - jsp, servlets and websocket, pom.xml, maven
web-fragment  - jsp , servlets, pom.xml, maaven
oms-tutorial  - springboot 2.2.7, pom.xml, oracledb 
vfunction-oms-tutorial - springboot 2.2.7, pom.xml, h2db


wecan build the ear directly if we have maven installed 

go to pom.xml folder and mvn clean isntall 
--------------------------------------------------
run instructions
--------------------------------------------------
https://bitbucket.org/vfunction/oms-tutorial/src/4ed6bb5b4bfe24fb0e5949c7c540008d95768949/oms-webmvc/
After cloning the repository, switch to oms-webmvc folder and compile using maven: mvn clean install

Running (in Tomcat)
Copy the .war file under the target folder to the webapps folder in Tomcat (example: cp ~/oms-tutorial/oms-webmvc/target/oms-0.0.1-SNAPSHOT.war /Library/Tomcat/webapps )
Restart Tomcat (run shutdown.sh and startup.sh in the bin directory of Tomcat)
To test that it works, go to the cloned repository. Switch to the script folder (e.g. ~/oms-webapp/script) and run ./use_apis.sh . You shoud see printouts with OK at the end.
Optionally, you can import the Postman collection under the script folder (OMS.postman_collection.json), review and run the various requests.
Viewing the H2 DB Console
To browse the database, go to to http://<server>:8080/oms-0.0.1-SNAPSHOT/admin/h2, where server is the address of the server (e.g. http://localhost:8080/oms-0.0.1-SNAPSHOT/admin/h2). Use sa/password as credentials and jdbc:h2:mem:testdb as the JDBC URL.

------------------------------------------------------------------
springboot application connect with oracldb 
------------------------------------------------------------------


path in matilda 
E:\imran-oms-apps\oms-tutorial\oms-webmvc\src\main\webapp\META-INF

but as main application
oms-tutorial\oms-webmvc\src\main\webapp\META-INF/context.xml

--------------------------------------------------------------
changes:
--------------------------------------------------------------

changes made in these below files 
 
spring-config.xml
--------------------
<property name="jpaProperties">
<props>
<prop key="hibernate.dialect">org.hibernate.dialect.Oracle12cDialect</prop>
</props>
<!--<props>
<prop key="hibernate.dialect">org.hibernate.dialect.H2Dialect</prop>
</props> -->
</property>
 
 
in schema.sql  ( oracle doe not have the DROP all objects command)
-------------------------------------------------------------------------
 
-- Drop sequence (first run only)
DROP SEQUENCE HIBERNATE_SEQUENCE;
 
-- Drop tables (first run only)
DROP TABLE SALES_ORDER CASCADE CONSTRAINTS;
DROP TABLE ORDER_LINE CASCADE CONSTRAINTS;
DROP TABLE CHARGES CASCADE CONSTRAINTS;
DROP TABLE PAYMENT_INFO CASCADE CONSTRAINTS;
DROP TABLE SHIP_TO_ADDRESS CASCADE CONSTRAINTS;
DROP TABLE BILL_TO_ADDRESS CASCADE CONSTRAINTS;
DROP TABLE INVENTORY CASCADE CONSTRAINTS;
DROP TABLE SHIPPING CASCADE CONSTRAINTS;
DROP TABLE LINE_CHARGE CASCADE CONSTRAINTS;
DROP TABLE PRODUCT_INFO CASCADE CONSTRAINTS;
-- =====================================
-- CREATE SEQUENCE
-- =====================================

 
in context.xml
-----------------
 
<Context>
<!-- <ResourceLink name="jdbc/omsds" global="jdbc/omsds" type="javax.sql.DataSource" /> -->
<!-- <Resource name="jdbc/omsds" auth="Container" type="javax.sql.DataSource"
              maxTotal="100" maxIdle="30" maxWaitMillis="10000"
              username="sa" password="password" driverClassName="org.h2.Driver"
              url="jdbc:h2:mem:testdb"/>  -->

<Resource name="jdbc/omsds"
            auth="Container"
            type="javax.sql.DataSource"
            driverClassName="oracle.jdbc.OracleDriver"
            url="jdbc:oracle:thin:@//172.24.7.84:1521/ORCL"
            username="oms_user"
            password="Matilda1234"
            maxTotal="50"
            maxIdle="10"
            maxWaitMillis="10000"
            />
</Context>
 
 
 
in web.xml   ( commented h2 db related code)
-----------------
 
 
<!-- H2 Database Console for managing the app's database -->
<!-- <servlet>
<servlet-name>H2Console</servlet-name>
<servlet-class>org.h2.server.web.WebServlet</servlet-class>
<init-param>
<param-name>webAllowOthers</param-name>
<param-value>true</param-value>
</init-param>
<load-on-startup>2</load-on-startup>
</servlet>  -->
 
	
 
	<!-- H2 -->
<!-- <servlet-mapping>
<servlet-name>H2Console</servlet-name>
<url-pattern>/admin/h2/*</url-pattern>
</servlet-mapping>  -->
 
 
 
in oms/pom.xml   ( replaced h2 db dependency with oracle artifact)
---------------------
 
<dependency>
<groupId>com.oracle.database.jdbc</groupId>
<artifactId>ojdbc8</artifactId>
<version>19.8.0.0</version>
</dependency>
 