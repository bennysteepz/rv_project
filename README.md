
### CD INTO PARENT DIRECTORY OF CLIENT APP
1. `cd parent_directory_of_toy-app`

### CLONE STARTS, CLIENT APP AND RPS PLUGIN
2. `git clone https://github.com/bennysteepz/starts.git`

3. `git clone https://github.com/bennysteepz/toy-app.git`

4. `git clone https://github.com/bennysteepz/rv_project.git`

### INSTALL STARTS, CLIENT APP AND RPS PLUGIN
5. `cd starts`, `mvn install`, `cd ..`

6. `cd toy-app`, `mvn install`, `cd ..`

7. `cd rv_project`, `mvn install`, `cd ..`

### UPDATE CLIENT APP POM.XML 
8. `cd toy-app`

9. open pom.xml

10. add to <pluginManagement> section:

```
<!-- INSTALL PLUGIN WITH ARGUMENTS INCLUDED FOR MAVEN INVOKER -->
<plugin>
  <artifactId>maven-install-plugin</artifactId>
  <version>2.5.2</version>
  <configuration>
    <groupId>javamop-agent</groupId>
    <artifactId>javamop-agent</artifactId>
    <version>1.0</version>
    <packaging>jar</packaging>
    <file>../javamop-agent-bundle/agents/JavaMOPAgent.jar</file>
  </configuration>
  <executions>
    <execution>
      <id>install-jar</id>
      <goals>
        <goal>install-file</goal>
      </goals>
      <phase>generate-sources</phase>
    </execution>
  </executions>
</plugin>
```

11. add STARTS, Javamop and RPS plugin to <plugins> section:
```
<!-- INCLUDE: JAVAMOP PLUGIN -->
<plugin>
 <groupId>org.apache.maven.plugins</groupId>
 <artifactId>maven-surefire-plugin</artifactId>
 <version>2.22.1</version>
 <configuration>
   <argLine>-javaagent:${settings.localRepository}/javamop-agent/javamop-agent/1.0/javamop-agent-1.0.jar</argLine>
 </configuration>
</plugin>

<!-- INCLUDE: RPS PLUGIN -->
<plugin>
 <groupId>com.steeper.ben</groupId>
 <artifactId>myexample-maven-plugin</artifactId>
 <version>1.0-SNAPSHOT</version>
</plugin>

<!-- INCLUDE: STARTS PLUGIN FOR INVOKER TO USE-->
<plugin>
 <groupId>edu.illinois</groupId>
 <artifactId>starts-maven-plugin</artifactId>
 <version>1.3</version>
</plugin>
```

### RUN RPS PLUGIN COMMAND FROM CLIENT APP
12. `cd toy-app`

13. Command to run RPS plugin:

`mvn com.steeper.ben:myexample-maven-plugin:rps -DagentsPath="../javamop-agent-bundle/agents/" -DspecsPath="../rv_project/src/main/resources/specs.txt" -DaffectedClassesPath="../rv_project/src/main/resources/affectedClasses.txt"`



