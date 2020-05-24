package si.uni_lj.fri.pbd.miniapp3.models;

public class RecipeSummaryIM {
    private String strMeal;
    private String strMealThumb;
    private String idMeal;
    private String imageBitmap;

    public RecipeSummaryIM(String strMeal, String strMealThumb, String idMeal, String imageBitmap) {
        this.strMeal = strMeal;
        this.strMealThumb = strMealThumb;
        this.idMeal = idMeal;
        this.imageBitmap = imageBitmap;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public String getIdMeal() {
        return idMeal;
    }

    public String getImageBitmap() {
        return imageBitmap;
    }

    @Override
    public String toString() {
        return "RecipeSummaryIM {" +
                "strMeal='" + strMeal + '\'' +
                ", strMealThumb='" + strMealThumb + '\'' +
                ", idMeal='" + idMeal + '\'' +
                '}';
    }
}
