package model

import androidx.compose.ui.graphics.Color
import green
import honey
import orangeDark
import org.jetbrains.compose.resources.ExperimentalResourceApi
import pinkLight
import primary
import recipeappkmp.shared.generated.resources.*
import red
import sugar
import yellow

/**
 * Created by abdulbasit on 18/06/2023.
 */

@OptIn(ExperimentalResourceApi::class)
val recipesList = listOf(
    Recipe(
        id = 1,
        title = "Lemon Cheesecake",
        description = "Tart Lemon Cheesecake sits atop an almond-graham cracker crust to add a delightful nuttiness to the traditional graham cracker crust. Finish the cheesecake with lemon curd for double the tart pucker!",
        ingredients = listOf(
            "110 g digestive biscuits ",
            "50 g butter",
            "25 g light brown soft sugar ",
            "350g mascarpone",
            "75 g caster sugar ",
            "1 lemon, zested",
            "2 - 3 lemons, juiced(about 90 ml)"
        ),
        instructions = listOf(
            " Crush the digestive biscuits in a food bag with a rolling pin or in the food processor . Melt the butter in a saucepan, take off heat and stir in the brown sugar and biscuit crumbs .",
            " Line the base of a 20 cm loose bottomed cake tin with baking parchment.Press the biscuit into the bottom of the tin and chill in the fridge while making the topping.",
            " Beat together the mascarpone, caster sugar, lemon zest and juice, until smooth and creamy . Spread over the base and chillfor a couple of hours .",
        ),
        image = Res.drawable._01_lemon_cheesecake,
        bgImage = Res.drawable._01_lemon_cheesecake_bg,
        bgImageLarge = Res.drawable._01_lemon_cheesecake_bg_lg,
        bgColor = Color(0xffFFEF7D),
    ),
    Recipe(
        id = 2,
        title = "Macaroons",
        description =
        "Soft and chewy on the inside, crisp and golden on the outside — these are the perfect macaroons.",
        ingredients = listOf(
            "1 ¾ cups powdered sugar(210 g)",
            "1 cup almond flour(95 g), finely ground",
            "1 teaspoon salt, divided",
            "3 egg whites, at room temperature",
            "¼ cup granulated sugar(50 g)",
            "½ teaspoon vanilla extract",
            "2 drops pink gel food coloring",
        ),
        instructions = listOf(
            "Make the macarons= In the bowl of a food processor, combine the powdered sugar, almond flour, and ½ teaspoon of salt, and process on low speed, until extra fine. Sift the almond flour mixture through a fine-mesh sieve into a large bowl.",
            "In a separate large bowl, beat the egg whites and the remaining ½ teaspoon of salt with an electric hand mixer until soft peaks form. Gradually add the granulated sugar until fully incorporated. Continue to beat until stiff peaks form (you should be able to turn the bowl upside down without anything falling out).",
            "Add the vanilla and beat until incorporated. Add the food coloring and beat until just combined.",
            "Add about ⅓ of the sifted almond flour mixture at a time to the beaten egg whites and use a spatula to gently fold until combined. After the last addition of almond flour, continue to fold slowly until the batter falls into ribbons and you can make a figure 8 while holding the spatula up.",
            "Transfer the macaron batter into a piping bag fitted with a round tip.",
            "Place 4 dots of the batter in each corner of a rimmed baking sheet, and place a piece of parchment paper over it, using the batter to help adhere the parchment to the baking sheet.",
            "Pipe the macarons onto the parchment paper in 1½-inch (3-cm) circles, spacing at least 1-inch (2-cm) apart.",
            "Tap the baking sheet on a flat surface 5 times to release any air bubbles.",
            "Let the macarons sit at room temperature for 30 minutes to 1 hour, until dry to the touch.",
            "Preheat the oven to 300˚F (150˚C).",
            "Bake the macarons for 17 minutes, until the feet are well-risen and the macarons don’t stick to the parchment paper.",
            "Transfer the macarons to a wire rack to cool completely before filling.",
            "Make the buttercream= In a large bowl, add the butter and beat with a mixer for 1 minute until light and fluffy. Sift in the powdered sugar and beat until fully incorporated. Add the vanilla and beat to combine. Add the cream, 1 tablespoon at a time, and beat to combine, until desired consistency is reached.",
            "Transfer the buttercream to a piping bag fitted with a round tip.",
            "Add a dollop of buttercream to one macaron shell. Top it with another macaron shell to create a sandwich. Repeat with remaining macaron shells and buttercream.",
            "Place in an airtight container for 24 hours to “bloom”.",
        ),
        image = Res.drawable._05_macaroons,
        bgImage = null,
        bgColor = primary,
    ),
    Recipe(
        id = 3,
        title = "Cream Cupcakes",
        description =
        "Bake these easy vanilla cupcakes in just 35 minutes. Perfect for birthdays, picnics or whenever you fancy a sweet treat, they're sure to be a crowd-pleaser",
        ingredients = listOf(
            "",
        ),
        instructions = listOf(),
        image = Res.drawable._08_cream_cupcakes,
        bgColor = pinkLight,
    ),
    Recipe(
        id = 4,
        title = "Chocolate Cheesecake",
        description =
        "Treat family and friends to this decadent chocolate dessert. It's an indulgent end to a dinner party or weekend family meal",
        ingredients = listOf(
            "150g digestive biscuits (about 10)",
            "1 tbsp caster sugar",
            "45g butter, melted, plus extra for the tin",
            "150g dark chocolate",
            "120ml double cream",
            "2 tsp cocoa powder",
            "200g full-fat cream cheese",
            "115g caster sugar"
        ),
        instructions = listOf(
            "To make the biscuit base, crush the digestive biscuits with a rolling pin or blitz in a food processor, then tip into a bowl with the sugar and butter and stir to combine. Butter and line an 18cm springform tin and tip in the biscuit mixture, pushing it down with the back of a spoon. Chill in the fridge for 30 mins.",
            "To make the cheesecake, melt the chocolate in short bursts in the microwave, then leave to cool slightly. Whip the cream in a large bowl using an electric whisk until soft peaks form, then fold in the cocoa powder. Beat the cream cheese and sugar together, then fold in the cream mixture and the cooled chocolate.",
            "Spoon the cheesecake mixture over the biscuit base, levelling it out with the back of a spoon. Transfer to the freezer and freeze for 2 hrs, or until set. Remove from the tin and leave at room temperature to soften for about 20 mins before serving.",
        ),
        image = Res.drawable._02_chocolate_cake_1,
        bgColor = orangeDark,
    ),
    Recipe(
        id = 5,
        title = "Fruit Plate",
        description = "Melons - they're firmer so make a great base for the softer berries and fruits.Tropical fruit -the top of a pineapple can be included for height,while dragonfruit looks vibrant.",
        ingredients = listOf(""),
        instructions = listOf(""),
        image = Res.drawable._09_fruit_plate,
        bgColor = green,
    ),
    Recipe(
        id = 6,
        title = "Chocolate Donuts",
        description =
        "Moist and fluffy donuts that are baked, not fried, and full of chocolate. Covered in a thick chocolate glaze, these are perfect for any chocoholic who wants a chocolate version of this classic treat.",
        ingredients = listOf(
            "1 cup (140g) all-purpose flour",
            "1/4 cup (25g) unsweetened cocoa powder",
            "1/2 teaspoon baking powder",
            "1/2 teaspoon baking soda",
            "1/8 teaspoon salt",
            "1 large egg",
            "1/2 cup (100g) granulated sugar",
            "1/3 cup (80 ml) milk",
            "1/4 cup (60 ml) yogurt",
            "2 tablespoons (30g) unsalted butter, melted",
            "1/2 teaspoon vanilla extract",
        ),
        instructions = listOf(
            "Preheat oven to 350°F/180°. Grease a donut pan with oil or butter. Set aside.",
            "Make the donuts= Whisk together the flour, cocoa powder, baking powder, baking soda, and salt in a large bowl. Set aside.",
            "In a medium bowl whisk egg with sugar until well combined. Add milk, yogurt, melted butter and vanilla extract, and whisk until combined. Pour into the flour mixture and mix until just combined. The batter will be thick.",
            "Fill donut cavities with batter ¾ way full using a spoon or a piping bag (much easier). Cut a corner off the bottom of the bag and pipe the batter into each donut cup.",
            "Bake for 9–10 minutes or until a toothpick inserted into the center of the donuts comes out clean. Allow to cool for 5 minutes in pan, then remove donuts from pan and transfer to a wire rack. Allow to cool completely before glazing.",
            "Make the chocolate glaze= Melt the chocolate, heavy cream, and butter gently in the microwave (in 30-second intervals, stirring in between) or a double boiler until smooth. Dip the tops of the donuts into the chocolate glaze, and place on a cooling rack to set.",
            "Donuts are best eaten the same day or keep them for up to 3 days in the refrigerator.",
        ),
        image = Res.drawable._03_chocolate_donuts,
        bgImage = null,
        bgColor = sugar,
    ),
    Recipe(
        id = 7,
        title = "Strawberry Cake",
        description =
        "Jam-packed with fresh strawberries, this strawberry cake is one of the simplest, most delicious cakes you’ll ever make.",
        ingredients = listOf(
            "",
        ),
        instructions = listOf(
            "",
        ),
        image = Res.drawable._13_strawberry_powdered_cake,
        bgColor = red,
    ),
    Recipe(
        id = 8,
        title = "Fluffy Cake",
        description =
        "This is a very good everyday cake leavened with baking powder. It's relatively light — it isn't loaded with butter, and it calls for only 2 eggs and 2 percent milk. Mine was perfectly baked after 30 minutes. After 10 minutes on the cooling rack, the cake released from the pans easily.",
        ingredients = listOf(
            "1/2 cup (1 stick) unsalted butter, cut into 2-tablespoon pieces and softened; plus more for coating pans",
            "2 1/4 cups all-purpose flour, plus more for coating pans",
            "1 1/3 cups granulated sugar",
            "1 tablespoon baking powder",
            "1/2 teaspoon salt",
            "1 tablespoon vanilla extract",
            "1 cup 2 percent milk, room temperature",
            "2 large eggs, room temperature",
        ),
        instructions = listOf(
            "Gather the ingredients. Preheat the oven to 350 F.",
            "Butter and flour two 9-inch cake pans. If desired, line the bottom with a circle of parchment",
            "Combine the sugar, flour, baking powder, and salt in the bowl of a stand mixer fitted with the paddle attachment. Mix until the dry ingredients are combined.",
            "With the mixer on the lowest speed, add the butter one chunk at a time and blend until the mixture looks sandy, between 30 seconds and 1 minute. Scrape down the bowl and paddle with a rubber spatula.",
            "Add the vanilla extract and, with the mixer on low, pour in the milk. Stop and scrape, then mix for another minute.",
            "Add the first egg and mix on medium-low until completely incorporated. Add the second egg and do the same. Scrape down the bowl and mix until fluffy on medium speed, about 30 seconds.",
            "Pour the batter into the prepared pans and give each one a couple of solid taps on the countertop to release any air bubbles. Transfer the pans to the preheated oven.",
            "Bake for about 30 minutes, or until a toothpick inserted into the center comes out clean or with a crumb or two attached. The tops will be golden brown, the edges will pull away from the sides of the pan, and the cakes will spring back when you touch them.",
            "Cool the cakes in their pans on a wire rack for 10 minutes, then loosen the edges by running a knife along the sides of the pan. Turn the cakes out onto the racks and cool for at least 1 hour before frosting.",
            "Frost with your choice of frosting and enjoy.",
        ),
        image = Res.drawable._04_fluffy_cake,
        bgImage = null,
        bgColor = orangeDark,
    ),
    Recipe(
        id = 9,
        title = "White Cream Cake",
        description =
        "This White Chocolate Cake is both decadent and delicious! White chocolate is incorporated into the cake layers, the frosting, and the drip for a stunning monochrome effect.",
        ingredients = listOf(
            "2 ½ cups all-purpose flour",
            "1 teaspoon baking soda",
            "½ teaspoon baking powder",
            "½ teaspoon salt",
            "6 (1 ounce) squares white chocolate, chopped",
            "½ cup hot water",
            "1 cup butter, softened",
            "1 ½ cups white sugar",
            "3 eggs",
            "1 cup buttermilk",
            "6 (1 ounce) squares white chocolate, chopped",
            "2 ½ tablespoons all-purpose flour",
            "1 cup milk",
            "1 cup butter, softened",
            "1 cup white sugar",
            "1 teaspoon vanilla extract",
        ),
        instructions = listOf(
            "Preheat oven to 350 degrees F (175 degrees C). Sift together the 2 1/2 cups flour, baking soda, baking powder and salt. Set aside.",
            "In small saucepan, melt 6 ounces white chocolate and hot water over low heat. Stir until smooth, and allow to cool to room temperature.",
            "In a large bowl, cream 1 cup butter and 1 1/2 cup sugar until light and fluffy. Add eggs one at a time, beating well with each addition. Stir in flour mixture alternately with buttermilk. Mix in melted white chocolate.",
            "Pour batter into two 9 inch round cake pans. Bake for 30 to 35 minutes in the preheated oven, until a toothpick inserted into the center of the cake comes out clean.",
            "To make Frosting= In a medium bowl, combine 6 ounces white chocolate, 2 1/2 tablespoons flour and 1 cup milk. Cook over medium heat, stirring constantly, until mixture is very thick. Cool completely.",
            "In large bowl, cream 1 cup butter, 1 cup sugar and 1 teaspoon vanilla; beat until light and fluffy. Gradually add cooled white chocolate mixture. Beat at high speed until it is the consistency of whipped cream. Spread between layers, on top and sides of cake.",
        ),
        image = Res.drawable._06_white_cream_cake,
        bgImage = null,
        bgColor = sugar,
    ),
    Recipe(
        id = 10,
        title = "Fruit Pie",
        description =
        "Bake a hearty fruit pie for dessert. Our collection of year-round pastry classics includes apple & blackberry, summer berries, lemon meringue and mince pies.",
        ingredients = listOf(
            "",
        ),
        instructions = listOf(
            "",
        ),
        image = Res.drawable._14_fruit_pie,
        bgImage = null,
        bgColor = yellow,
    ),
    Recipe(
        id = 11,
        title = "Honey Cake",
        description =
        "The secret to this cake ' s fantastic flavor is the tiny amount of bitterness from burnt honey.The slightly tangy whipped cream frosting provides a bit of acidity and lovely light texture, and unlike other frostings, it' s not too sweet",
        ingredients = listOf(
            "¾ cup wildflower honey",
            "3 tablespoons cold water",
            "1 cup white sugar",
            "14 tablespoons unsalted butter, cut into slices",
            "¾ cup wildflower honey",
            "2 ½ teaspoons baking soda",
            "1 teaspoon ground cinnamon",
            "¾ teaspoon fine salt",
            "6 large cold eggs",
            "3 ¾ cups all-purpose flour",
        ),
        instructions = listOf(
            "Pour 3/4 cup wildflower honey into a deep saucepan over medium heat. Boil until a shade darker and caramel-like in aroma, about 10 minutes. Turn off heat and whisk in cold water.",
            "Preheat the oven to 375 degrees F (190 degrees C). Line a baking sheet with a silicone baking mat. Place a mixing bowl and whisk in the refrigerator.",
            "Place a large metal bowl over the lowest heat setting on the stovetop. Add sugar, butter, 3/4 cup wildflower honey, and 1/4 cup burnt honey. Let sit until butter melts, 5 to 7 minutes. Reserve remaining burnt honey for the frosting.",
            "Meanwhile, combine baking soda, cinnamon, and salt in a small bowl.",
            "Whisk butter mixture and let sit until very warm to the touch. Whisk in eggs. Keep mixture over low heat until it warms up again, then whisk in baking soda mixture. Remove from heat. Sift in flour in 2 or 3 additions, stirring well after each, until batter is easily spreadable.",
            "Transfer about 1/2 cup batter onto the prepared baking sheet. Spread into an 8- or 9-inch circle using an offset spatula. Shake and tap the pan to knock out any air bubbles.",
            "Bake in the preheated oven until lightly browned, 6 to 7 minutes. Remove liner from the pan and let cake layer continue cooling until firm enough to remove, 6 to 7 minutes. Invert cake onto a round of parchment paper.",
            "Repeat Steps 6 and 7 until you have 8 cake layers, letting each cool on an individual parchment round. Trim edges using a pizza wheel to ensure they are the same size; save scraps for crumb mixture.",
            "Spread any remaining batter onto the lined baking sheet. Bake in the preheated oven until edges are dry, about 10 minutes. Remove from the oven and cut into small pieces; toss with reserved cake scraps.",
            "Return scraps to the oven and bake until browned, 7 to 10 minutes more. Let cool completely, 15 to 20 minutes. Transfer to a resealable bag and beat into fairly fine crumbs using a rolling pin. Set aside.",
            "Remove the bowl and whisk from the refrigerator. Pour in heavy cream and whisk until soft peaks form. Add sour cream and remaining burnt honey; continue whisking until stiff peaks form.",
            "Place a cake layer on a parchment paper round on a pizza pan or serving plate. Spread a cup of frosting evenly on top, almost to the edge. Repeat with cake layers and frosting, pressing layers in smooth-side down. Place last cake layer smooth-side up. Frost top and sides of cake. Cover with crumbs; clean any excess crumbs around base.",
            "Cover with plastic wrap and refrigerate for at least 8 hours to overnight. Transfer to a cake stand using 2 spatulas. Cut and serve.",
        ),
        image = Res.drawable._07_honey_cake,
        bgImage = null,
        bgColor = honey,
    ),
    Recipe(
        id = 12,
        title = "Powdered Cake",
        description =
        "Heavy on the butter and nutmeg, this cake has all the flavors of your favorite cake donut in a convenient square shape.",
        ingredients = listOf(
            "",
        ),
        instructions = listOf(
            "",
        ),
        image = Res.drawable._11_powdered_cake,
        bgColor = sugar,
    ),
    Recipe(
        id = 13,
        title = "Strawberries",
        description =
        "We' ll admit it = we go a little crazy during strawberry season.Though easy to grow, these sweet berries just taste better when you get them in season, as opposed to buying them at other times of the year.",
        ingredients = listOf(
            "",
        ),
        instructions = listOf(
            "",
        ),
        image = Res.drawable._10_strawberries,
        bgColor = red,
    ),
    Recipe(
        id = 14,
        title = "Chocolate Cake",
        description =
        "The Best Chocolate Cake Recipe – A one bowl chocolate cake recipe that is quick, easy, and delicious! Updated with gluten-free, dairy-free, and egg-free options!",
        ingredients = listOf(
            "",
        ),
        instructions = listOf(
            "",
        ),
        image = Res.drawable._12_chocolate_cake_2,
        bgColor = orangeDark,
    ),
    Recipe(
        id = 15,
        title = "Apple Pie",
        description =
        "This was my grandmother' s apple pie recipe.I have never seen another one quite like it.It will always be my favorite and has won me several first place prizes in local competitions.",
        ingredients = listOf(
            "",
        ),
        instructions = listOf(
            "",
        ),
        image = Res.drawable._15_apple_pie,
        bgColor = sugar,
    )
)