# casal  

###Makes life with CASA easier  

    val json = Source.fromURL("http://casa.com/out.payloads").mkString
    
    val entities = parse(json).extract[List[Entity]]
    val translated = entities.map(Casa.Transforms.ADJINTRANSLATE)
    val squashed = translated.map(Casa.Transforms.ADJINSQUASH)  
    
###To install
    git clone git@github.com:learningobjectsinc/casal.git
    cd casal
    sbt compile
    sbt publish    #installs casal to ~/.m2/

###Include in your project:
If you're using scala/sbt, you can include this project with:

    libraryDependencies += "com.learningobjects" %% "casal" % "1.0.0"
    
If you're using maven/java, you can include casal like:

    <dependency>
	  <groupId>com.learningobjects</groupId>
	  <artifactId>casal</artifactId>
	  <version>1.0.0</version>
    </dependency>

