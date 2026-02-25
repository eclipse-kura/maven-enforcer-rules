# maven-enforcer-rules

Additional ruleset to apply to [org.apache.maven.plugins:maven-enforcer-plugin](https://maven.apache.org/enforcer/).

Minimum requirements:

- Java >= 17
- Maven Enforcer Plugin >=3.6.2, <4.0.0
- Maven >= 3.9.10

## Contributing

New rules can be added by creating classes that extend `AbstractEnforcerRule`. For simplicity, keep them under the same package and project, so that all rules are deployed and usable under the same package.

Integration tests are found under `src/it`; to add a new test simply add a new folder with a `pom.xml` test case and a `invoker.properties` containing the expected build result.

The plugin can be installed with:

```shell
mvn clean install
```

and, by default, will also run the tests and create a JaCoCo report under `target/site/jacoco`.

## Available plugins

### bannedPluginsAdvanced

**Parameters**:

- `message` (optional): a message to display on ban
- `bannedPlugins` (required): a list of plugins to ban, `bannedPlugin` contains:
    - `groupId` (required): the group id of the banned plugin
    - `artifactId` (required): the artifact id of the banned plugin
    - `version`: (optional): the version of the banned plugin. If not specified it will allow all versions
    - `goal` (optional): specific goal to ban. If left empty it will allow all the goals

If you want to ban more versions (or goals) of the same plugin, just repeat the `<bannedPlugin>` with all the desired combinations.

**Usage**:

```xml
<build>
    <plugins>
        <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-enforcer-plugin</artifactId>
        <version>3.6.2</version>
        <dependencies>
            <dependency>
                <groupId>org.eclipse.kura</groupId>
                <artifactId>maven-enforcer-rules</artifactId>
                <version>1.1.0-SNAPSHOT</version>
            </dependency>
        </dependencies>
        <executions>
            <execution>
                <id>enforce</id>
                <configuration>
                    <rules>
                        <bannedPluginsAdvanced>
                            <message>The used plugins are not allowed in this build</message>
                            <bannedPlugins>
                                <bannedPlugin>
                                    <groupId>org.apache.maven.plugins</groupId>
                                    <artifactId>maven-dependency-plugin</artifactId>
                                    <version>3.4.0</version>
                                    <goal>copy</goal>
                                </bannedPlugin>
                            </bannedPlugins>
                        </bannedPluginsAdvanced>
                    </rules>
                </configuration>
                <goals>
                    <goal>enforce</goal>
                </goals>
            </execution>
        </executions>
        </plugin>
    </plugins>
</build>
```