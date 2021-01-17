package tacos.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import tacos.Taco;
import tacos.Order;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.User;
import tacos.data.IngredientRepository;

import javax.validation.Valid;
import org.springframework.validation.Errors;

import tacos.data.TacoRepository;
import tacos.data.UserRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
@CrossOrigin(origins="*")
public class DesignTacoController {

    private final IngredientRepository ingredientRepo;

    private TacoRepository tacoRepo;

    private UserRepository userRepo;

    @Autowired
    public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository tacoRepo, UserRepository userRepo) {
        this.ingredientRepo = ingredientRepo;
        this.tacoRepo = tacoRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public String showDesignForm(Model model, Principal principal) {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredientRepo.findAll().forEach(i -> ingredients.add(i));

        Type[] types = Ingredient.Type.values();
        for(Type type : types) {
            model.addAttribute(type.toString().toLowerCase(),
                    filterByType(ingredients, type));
        }

        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);

        return "design";
    }

    private List<Ingredient> filterByType(
            List<Ingredient> ingredients, Type type) {
                return ingredients
                        .stream()
                        .filter(x -> x.getType().equals(type))
                        .collect(Collectors.toList());
    }

    @ModelAttribute(name = "order")
    public Order order() {
        return new Order();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors, @ModelAttribute Order order) {
        if (errors.hasErrors()) return "design";

        Taco saved = tacoRepo.save(design);
        order.addDesign(saved);

        return "redirect:/orders/current";
    }

    @GetMapping("/recent")
    @ResponseBody
    public CollectionModel<EntityModel<Taco>> recentTacos() {
        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        log.info("최근 저장된 타코 불러오는 중...");

        List<Taco> optTacos = tacoRepo.findAll(page).getContent();

        CollectionModel<EntityModel<Taco>> recentResources = CollectionModel.wrap(optTacos);

        recentResources.add(
                WebMvcLinkBuilder.linkTo(DesignTacoController.class)
                    .slash("recent")
                    .withRel("recents"));
        /*
        recentResoureces.add(
                linkTo(methodOn(DesignTacoController.class).recentTacos())
                    .withRel("recents"));
         */

        return recentResources;

    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Taco> tacoById(@PathVariable("id") Long id) {
        Optional<Taco> optTaco = tacoRepo.findById(id);
        log.info("path 변수 사용해서 id에 맞는 타코 가져오기");
        if (optTaco.isPresent()) {
            return new ResponseEntity<>(optTaco.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Taco postTaco(@RequestBody Taco taco) {
        return tacoRepo.save(taco);
    }

}
