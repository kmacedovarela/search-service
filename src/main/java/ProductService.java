import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;



@ApplicationScoped
public class ProductService {

    @Inject
    RestClient restClient;

    public List<Product> search(String name, String description) throws IOException {
        Request request = new Request(
                "GET",
                "/products/_search");
        //construct a JSON query like {"query": {"match": {"<term>": "<match"}}
        JsonObject matchJson = new JsonObject();

        if(!name.isEmpty()) {
            JsonObject nameJson = new JsonObject().put("name", name);
            matchJson.put("match", nameJson);
        }
        if(!description.isEmpty()) {
            JsonObject descriptionJson = new JsonObject().put("description", description);
            matchJson.put("match", descriptionJson);
        }
        JsonObject queryJson = new JsonObject().put("query", matchJson);

        request.setJsonEntity(queryJson.encode());
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        JsonObject json = new JsonObject(responseBody);
        JsonArray hits = json.getJsonObject("hits").getJsonArray("hits");
        List<Product> results = new ArrayList<>(hits.size());
        for (int i = 0; i < hits.size(); i++) {
            JsonObject hit = hits.getJsonObject(i);
            Product product = hit.getJsonObject("_source").mapTo(Product.class);
            results.add(product);
        }
        return results;
    }
//    private List<Product> search(String term, String match) throws IOException {
//        Request request = new Request(
//                "GET",
//                "/products/_search");
//        //construct a JSON query like {"query": {"match": {"<term>": "<match"}}
//        JsonObject termJson = new JsonObject().put(term, match);
//        JsonObject matchJson = new JsonObject().put("match", termJson);
//        JsonObject queryJson = new JsonObject().put("query", matchJson);
//        request.setJsonEntity(queryJson.encode());
//        Response response = restClient.performRequest(request);
//        String responseBody = EntityUtils.toString(response.getEntity());
//
//        JsonObject json = new JsonObject(responseBody);
//        JsonArray hits = json.getJsonObject("hits").getJsonArray("hits");
//        List<Product> results = new ArrayList<>(hits.size());
//        for (int i = 0; i < hits.size(); i++) {
//            JsonObject hit = hits.getJsonObject(i);
//            Product product = hit.getJsonObject("_source").mapTo(Product.class);
//            results.add(product);
//        }
//        return results;
//    }
}
