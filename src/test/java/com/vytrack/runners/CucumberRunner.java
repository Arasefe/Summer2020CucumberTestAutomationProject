package com.vytrack.runners;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "rerun:target/rerun.txt",
                "json:target/cucumber.json"
        },
        features = {
                "src/test/resources/features/activities",
        },
        glue = "com/vytrack/step_definitions",
        dryRun = false,
        tags = "@add_car",
        publish = true
)
public class CucumberRunner {
    //tags = "@s_o and @with_two_columns",
    // scenario must have BOTH tags
    //java logic if(@s_o && @with_two_columns)

    //tags = "@s_o or @with_two_columns",
    // scenario must have EITHER OR tags
    //java logic if(@s_o || @with_two_columns),
}
