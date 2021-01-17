//package tacos.web;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
//
//import org.springframework.hateoas.server.RepresentationModelProcessor;
//import tacos.Taco;
//
//@Configuration
//public class SpringDataRestConfiguration {
//
//    public class PaymentProcessor implements RepresentationModelProcessor<EntityModel<Taco>> {
//
//        @Override
//        public EntityModel<Taco> process(EntityModel<Taco> model) {
//            model.add(
//                    WebMvcLinkBuilder.linkTo(Taco.class)
//                            .slash("recent")
//                            .withRel("recents"));
//            return model;
//        }
//    }
//
//    @Bean
//    PaymentProcessor paymentProcessor() {
//        return new PaymentProcessor();
//    }
//}


