package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import tacos.Ingredient;
import tacos.data.IngredientRepository;

@Slf4j
@Controller
@RequestMapping("/ingredient")
public class IngredientController {
    private IngredientRepository ingredientRepo;

    @Autowired
    public IngredientController(IngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

    @GetMapping
    @ResponseBody
    public Iterable<Ingredient> allIngredients() {
        return ingredientRepo.findAll();
    }

    RestTemplate rest = new RestTemplate();

//    @GetMapping("/{id}")
//    @ResponseBody
//    public Ingredient getIngredientById(@PathVariable("id") String id) {
//
//    }

}
