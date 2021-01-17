package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import tacos.Taco;
import tacos.data.TacoRepository;
import tacos.model.TacoModel;
import tacos.model.TacoModelAssembler;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/recent")
@CrossOrigin(origins="*")
public class RecentController {
    private TacoRepository tacoRepo;

    @Autowired
    public RecentController(TacoRepository tacoRepo) {
        this.tacoRepo = tacoRepo;
    }

    @GetMapping()
    public CollectionModel<TacoModel> recentTacos() {
        PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        log.info("최근 저장된 타코 불러오는 중...");

        List<Taco> tacos = tacoRepo.findAll(page).getContent();

        log.info("관련 링크 만들기(하드코딩하지 않기 위해)");
        CollectionModel<TacoModel> recentModels = new TacoModelAssembler().toCollectionModel(tacos);

        recentModels.add(
                WebMvcLinkBuilder.linkTo(RecentController.class)
                        .withRel("recents"));

        return recentModels;

    }
}
