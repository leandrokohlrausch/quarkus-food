package br.com.food.resource;

import br.com.food.controller.FoodController;
import br.com.food.entity.FoodEntity;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/food")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FoodResource {

    @Inject
    private FoodController foodController;

    @GET
    public List<FoodEntity> findAll() {
        return FoodEntity.listAll();
    }

    @POST
    @Transactional
    public Response create(FoodEntity food) {
        FoodEntity.persist(food);
        return Response.ok(food).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, FoodEntity food) {
        if (foodController.isFoodNameIsNotEmpty(food)) {
            return Response.ok("Food was not found").type(MediaType.APPLICATION_JSON_TYPE).build();
        }
        FoodEntity foodEntity = foodController.update(id, food);
        return Response.ok(foodEntity).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        FoodEntity foodEntity = FoodEntity.findById(id);
        if (foodEntity == null) {
            throw new WebApplicationException("Food with id " + id + " does not exist.", Response.Status.NOT_FOUND);
        }
        foodEntity.delete();
        return Response.status(204).build();
    }
}