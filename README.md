# newhost2
###### Reactive development platform
This project based on <a href="http://vertx.io" target="_black">vert.x</a> toolkit and simplify web development.

# Controller example

If you send request for the first time then will be created blank page (<code>index.html</code>) where you can put your content. 
No need to create that page manually.

```java
public class MainMenuController {

              /**
              * Performs requests for index page (http://somesite.com or http://somesite.com/index)
              */
              @GET // request type
              @Path(value = {"/", "/index"})
              public Result index() {
                  // do some logic
                  return Result.HTML(); // response html page to browser
              }
}
```


