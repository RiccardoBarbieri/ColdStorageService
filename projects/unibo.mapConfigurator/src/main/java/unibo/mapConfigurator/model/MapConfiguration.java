package unibo.mapConfigurator.model;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MapConfiguration {

    @NotNull(message = "Map name cannot be null")
    @NotBlank(message = "Map name cannot be blank")
    private String name;

    @NotNull(message = "Map compact cannot be null")
    @NotBlank(message = "Map compact cannot be blank")
    @Size(min = 1, message = "Map compact must be width x height")
    private String compact;

    @NotNull(message = "Map width cannot be null")
    @Min(value = 1, message = "Map width must be greater than 0")
    private Integer width;

    @NotNull(message = "Map height cannot be null")
    @Min(value = 1, message = "Map height must be greater than 0")
    private Integer height;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompact() {
        return compact;
    }

    public void setCompact(String compact) {
        this.compact = compact;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "MapConfiguration{" +
                "name='" + name + '\'' +
                ", compact='" + compact + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
