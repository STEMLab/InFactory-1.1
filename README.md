# InFactory-1.1  
InFactory-1.1 is for creating & editing OGC IndoorGML 1.1 formatted data. This project is an upgrade version from [InFactory](https://github.com/STEMLab/InFactory) to support IndoorGML 1.1. 

`Note that the main difference of IndoorGML 1.1 from the previous one (v.1.0.3) is the inclusion of the "level" property of CellSpace. Since "level" property is an optional and cardinality is [0..*], no conflict occurs with IndoorGML 1.0.3.` 

## Getting Started

### Prerequisites
1) OS: regardless of OS  
2) Maven: over 3.5 version. You need to install Maven.
   * This project supports the Maven wrapper. If you do not want to install Maven, follow `With Maven wrapper`.
3) JDK: over 11 version. 
  
### Installing and Running
#### With installed Maven
1) Project build `mvn clean install`
2) Spring server execute `mvn jetty:run` 
3) We recommend using the other port number(e.g.,9797) with parameter
`mvn jetty:run "-Djetty.port=9797"`

#### With Maven wrapper
1) Project build `./mvnw clean install`
2) Spring server execute `./mvnw jetty:run` 
3) We recommend using the other port number(e.g.,9797) with parameter
`./mvnw jetty:run "-Djetty.port=9797"` 
   
## Built With
* [Maven](https://maven.apache.org/) - Dependency Management
* [Spring Framework 5.0](https://spring.io/) - Java web framework

## How to make HTTP requests
It is explained at the [Wiki](https://github.com/STEMLab/InFactory/wiki).  

## Developer
* Hyemi Jeong - IndoorGML CRUD DAO developer 
* Hyung-Gyu Ryoo - IndoorGML Restful API developer 
* **Do-Hoon Kang - InFactory Server developer and maintainer, dhkang@pnu.edu**
* **Taehoon Kim - InFactory Server developer and maintainer, taehoon.kim@pnu.edu**

## Contributing
Please refer to each project's style guidelines and guidelines for submitting patches and additions. In general, we follow the "fork-and-pull" Git workflow.

1) Fork the repo on GitHub.
2) Clone the project to your own machine.
3) Commit changes to your own branch.
4) Push your work back up to your fork.
5) Submit a Pull request so that we can review your changes.

`NOTE: Be sure to merge the latest from "upstream" before making a pull request!`

## License 
This project is under the MIT License - see the [LICENSE](https://github.com/STEMLab/InFactory-1.1/blob/master/LICENSE)

## More Information
More information can check at [Wiki](https://github.com/STEMLab/InFactory/wiki).
