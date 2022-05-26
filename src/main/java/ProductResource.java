import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.RestQuery;

@Path("/products")
public class ProductResource {

    @Inject
    ProductService productService;

    @GET
    @Path("/search")
    public List<Product> search(@RestQuery @DefaultValue("") String name, @RestQuery @DefaultValue("") String description) throws IOException {
        if(name != null || description != null){
            return productService.search(name, description);
        } else {
            throw new BadRequestException("Should provide name or description query parameter");
        }
    }
}
