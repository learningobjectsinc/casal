# casal  

###Makes life with CASA easier  

``` scala
val json = Source.fromURL("http://casa.com/out.payloads").mkString

val entities = parse(json).extract[List[Entity]]
val translated = entities.map(Casa.Transforms.ADJINTRANSLATE)
val squashed = translated.map(Casa.Transforms.ADJINSQUASH)
```

###Include in your project:
If you're using scala/sbt, you can include this project with:

``` scala
resolvers += "Learning Objects Bintray Repo" at "https://dl.bintray.com/learningobjectsinc/repo"

libraryDependencies += "com.learningobjects" %% "casal" % "1.0.0"
```
    
If you're using maven/java, you can include casal like:

``` xml
<repositories>
    <repository>
        <id>learningobjects-bintray</id>
        <url>https://dl.bintray.com/learningobjectsinc/repo</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
    ...
</repositories>

<dependencies>
    <dependency>
        <groupId>com.learningobjects</groupId>
        <artifactId>casal_2.11</artifactId>
        <version>1.0.0</version>
    </dependency>
    ...
</dependencies>
```

###To install locally
    git clone git@github.com:learningobjectsinc/casal.git
    cd casal
    sbt compile
    sbt publish    #installs casal to ~/.m2/