package app3d_3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MapaPanel extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener {
    private BufferedImage mapa;
    private List<PaisRegion> regiones = new ArrayList<>();
    private List<PaisRegion> regionesOriginales = new ArrayList<>();
    private PaisRegion hoverRegion = null;
    private double escala = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;

    private final double ESCALA_MIN = Math.max(1200.0 / 1200, 700.0 / 784);
    private final double ESCALA_MAX = 3.0;

    private JButton zoomInBtn, zoomOutBtn, volverBtn,muteBtn;
    private Point dragStart;

    public MapaPanel() {
        this(false);
    }

    public MapaPanel(boolean conBotonVolver) {
        try {
            mapa = ImageIO.read(new File("mapa_base.png"));
        } catch (IOException e) {
            System.err.println("No se pudo cargar la imagen del mapa.");
        }
        ImageIcon unmute = new ImageIcon(getClass().getResource("icons8-megáfono-40.png"));
        Image imagen16 = unmute.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon icono16 = new ImageIcon(imagen16);
        
        ImageIcon mute = new ImageIcon(getClass().getResource("megafonoSinEscuchar.png"));
        Image imagen15 = mute.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon icono15 = new ImageIcon(imagen15);
        
        JButton sonidoBtn = new JButton(icono16);     

        sonidoBtn.setBounds(300, 10, 24, 24);
        add(sonidoBtn);
        
        Sonido musica = Sonido.getInstancia();
        
        sonidoBtn.addActionListener(e -> {
        if (musica.estaSonando()) {
        sonidoBtn.setIcon(icono15);
        musica.detenerSonido(); // mute
        } else {
        sonidoBtn.setIcon(icono16);
        musica.reanudarSonido(); // unmute
        }
    });

        regionesOriginales.add(new PaisRegion("Estados Unidos", new Polygon(
            new int[]{173, 163, 187, 276, 288, 308, 300, 303, 356, 357, 293, 269, 235, 225, 217, 210, 194},
            new int[]{277, 237, 196, 210, 218, 233, 245, 250, 229, 238, 320, 301, 314, 296, 298, 286, 284},
            17), ClimaFetcher.obtenerClima("USA")));

        regionesOriginales.add(new PaisRegion("Venezuela", new Polygon(
            new int[]{320,312,315,329,331,335,340,346,343,343,350,355,354,358,349,342,330,325,321,320,316},
            new int[]{380, 389, 398, 404, 419, 425, 425, 419, 416, 412, 414, 408, 403, 396, 387, 387, 384, 381, 384, 390, 386},
            21), ClimaFetcher.obtenerClima("Venezuela")));
        
        regionesOriginales.add(new PaisRegion("Surinam", new Polygon(
            new int[]{369,366,370,370,377,380,375,372},
            new int[]{405,410,415,421,418,406,405,407},
            8),
            ClimaFetcher.obtenerClima("Surinam")));

        regionesOriginales.add(new PaisRegion("Brasil", new Polygon(
        new int[]{355,351,343,344,348,341,336,330,325,324,327,323,312,310,317,322,327,340,344,360,361,368,370,369,371,379,385,388,380,396,405,405,428,449,450,393,391,384,378,362,356},
        new int[]{410,415,412,417,419,427,426,421,422,430,433,444,449,460,465,462,470,464,475,480,489,489,497,505,512,513,520,530,544,553,540,525,517,465,446,423,410,421,419,424,417},
        41 ),
        ClimaFetcher.obtenerClima("Brasil")));
                regionesOriginales.add(new PaisRegion("Panama", new Polygon(
        new int[]{278,277,287,288,296,299,290,284},
        new int[]{389,397,399,395,396,391,387,392},
        8),
        ClimaFetcher.obtenerClima("Panama")));

                regionesOriginales.add(new PaisRegion("Mexico", new Polygon(
        new int[]{172,176,190,214,244,249,249,263,270,266,252,243,235,235,229,225,220,215,212,193,183},
        new int[]{285,309,329,360,368,361,356,353,342,338,340,345,335,321,316,301,304,302,293,291,283},
        21),
        ClimaFetcher.obtenerClima("Mexico")));
                regionesOriginales.add(new PaisRegion("Honduras", new Polygon(
        new int[]{258,262,274,277,271,264,261,261},
        new int[]{369,364,363,367,368,378,377,372},
        8),
        ClimaFetcher.obtenerClima("Honduras")));
                regionesOriginales.add(new PaisRegion("Nicaragua", new Polygon(
        new int[]{277,271,264,278,275,266},
        new int[]{367,368,378,376,384,383},
        6),
        ClimaFetcher.obtenerClima("Nicaragua")));
                regionesOriginales.add(new PaisRegion("Niger ", new Polygon(
        new int[]{591, 587, 586, 574, 581, 585, 588, 593, 597, 610, 621, 621, 627, 630},
        new int[]{357, 357, 369, 371, 382, 381, 377, 376, 380, 380, 373, 369, 363, 350},14),
        ClimaFetcher.obtenerClima("Niger")));
                regionesOriginales.add(new PaisRegion("Groelandia", new Polygon(
        new int[]{404,426,527,558,476},
        new int[]{54,189,128,54,25},
        5),
        ClimaFetcher.obtenerClima("greenland")));
                regionesOriginales.add(new PaisRegion("nigeria", new Polygon(
        new int[]{582,587,595,603,609,614,623,618,609,600,592,584,587},
        new int[]{403,403,410,409,401,401,379,375,379,380,376,381,391},13),
        ClimaFetcher.obtenerClima("nigeria")));
                regionesOriginales.add(new PaisRegion("Belice", new Polygon(
        new int[]{258,258,260,263},
        new int[]{356,361,360,354},
        4),
        ClimaFetcher.obtenerClima("Belize")));
        
        regionesOriginales.add(new PaisRegion("Ecuador", new Polygon(
                new int[]{290,284,283,285,285,291,302,301,296},
                new int[]{423,427,435,438,444,447,435,429,428},
                9),
                ClimaFetcher.obtenerClima("Ecuador")));
        regionesOriginales.add(new PaisRegion("Peru", new Polygon(
            new int[]{281,281,302,324,328,329,321,317,309,311,318,320,311,305,294,284},
            new int[]{442,448,486,500,494,478,463,466,459,449,444,440,438,430,446,445},
            16),
            ClimaFetcher.obtenerClima("Peru")));

        regionesOriginales.add(new PaisRegion("Paraguay", new Polygon(
            new int[]{358,355,360,367,373,371,380,384,384,379,372,371,368},
            new int[]{504,515,520,521,525,534,534,527,520,513,513,506,503},
            13),
            ClimaFetcher.obtenerClima("Paraguay")));

        regionesOriginales.add(new PaisRegion("Bolivia", new Polygon(
            new int[]{326,330,329,328,335,338,342,354,358,368,369,367,361,360,343,340,334},
            new int[]{470,480,495,500,517,518,514,514,504,503,496,489,489,480,475,466,466},
            17),
            ClimaFetcher.obtenerClima("Bolivia")));

        regionesOriginales.add(new PaisRegion("Uruguay", new Polygon(
            new int[]{379,376,377,385,396},
            new int[]{545,548,563,565,555},
            5),
            ClimaFetcher.obtenerClima("Uruguay")));

        regionesOriginales.add(new PaisRegion("Argentina", new Polygon(
            new int[]{340,335,331,337,335,343,346,350,357,364,383,377,375,390,385,380,372,373,365,360,353,343},
            new int[]{520,526,549,565,579,615,640,646,646,623,571,564,547,530,527,534,534,526,520,520,515,514},
            22),
            ClimaFetcher.obtenerClima("Argentina")));

        regionesOriginales.add(new PaisRegion("Guyana", new Polygon(
            new int[]{359,355,356,356,360,368,369,365,369},
            new int[]{396,403,409,418,425,422,416,410,404},
            9),
            ClimaFetcher.obtenerClima("Guyana")));
        regionesOriginales.add(new PaisRegion("tunez ", new Polygon(
        new int[]{599,601,600,604,603,608,608,613,608,607},
        new int[]{286,295,300,305,311,311,307,301,298,284},10),
        ClimaFetcher.obtenerClima("tunez")));


        regionesOriginales.add(new PaisRegion("libia", new Polygon(
        new int[]{614,609,607,605,607,612,615,626,630,660,657,649,642,638,640,634,623},
        new int[]{301,307,312,331,335,335,338,341,340,353,307,302,302,305,310,312,304},17),
        ClimaFetcher.obtenerClima("libia")));

        // Colombia        
        regionesOriginales.add(new PaisRegion("Colombia", new Polygon(
            new int[]{319,297,298,293,305,312,320,320,325,328,324,324,331,330,317,313,320},
            new int[]{378, 400, 410, 425, 429, 439, 440, 445, 445, 433, 430, 422, 422, 405, 400, 391, 381},
            17), ClimaFetcher.obtenerClima("Colombia")));

        regionesOriginales.add(new PaisRegion("Guyana Francesa", new Polygon(
        new int[]{381,380,379,383,390},
        new int[]{406,410,417,420,410},
        5),
        ClimaFetcher.obtenerClima("Cayenne")));

        regionesOriginales.add(new PaisRegion("Costa Rica", new Polygon(
        new int[]{266,266,275,278,274},
        new int[]{384,388,395,390,384},
        5),
        ClimaFetcher.obtenerClima("9.93,-84.08")));

        regionesOriginales.add(new PaisRegion("El Salvador", new Polygon(
            new int[]{254, 256, 260, 260},
            new int[]{374, 370, 372, 377},
            4),
            ClimaFetcher.obtenerClima("13.6894,-89.1872")
        ));
        regionesOriginales.add(new PaisRegion("Guatemala", new Polygon(
        new int[]{246,254,261,257,256,249,251},
        new int[]{368,373,363,361,355,357,362},
        7),
        ClimaFetcher.obtenerClima("Guatemala")));

        regionesOriginales.add(new PaisRegion("Canada", new Polygon(
            new int[]{187,277,310,302,332,350,367,385,402,387,376,371,374,355,340,333,325,325,294,325,328,345,356,349,336,328,313,288,261,241,212,198,164,177,183,175,180},
        new int[]{199, 214, 238, 253, 242, 235, 218, 222, 211, 171, 177, 173, 166, 153, 190, 191, 209, 189, 164, 135, 126, 127, 116, 104, 114, 87, 113, 98, 97, 74, 68, 63, 118, 130, 156, 167, 185},
            37),
            ClimaFetcher.obtenerClima("56.130366,-106.346771")));

        regionesOriginales.add(new PaisRegion("Cuba", new Polygon(
            new int[]{279, 301, 301, 313, 313, 288},
            new int[]{339, 345, 351, 348, 344, 334},
            6),
            ClimaFetcher.obtenerClima("21.5218,-77.7812")
        ));

        regionesOriginales.add(new PaisRegion("Jamaica", new Polygon(
            new int[]{298, 298, 305, 305},
            new int[]{353, 357, 357, 353},
            4),
            ClimaFetcher.obtenerClima("18.1096,-77.2975")
        ));

        regionesOriginales.add(new PaisRegion("Haití", new Polygon(
            new int[]{321, 321, 314, 311, 312, 314},
            new int[]{348, 357, 358, 357, 353, 347},
            6),
            ClimaFetcher.obtenerClima("18.9712,-72.2852")
        ));
        regionesOriginales.add(new PaisRegion("Republica Dominicana", new Polygon(
            new int[]{322, 322, 331, 332},
            new int[]{348, 357, 356, 352},
            4),
            ClimaFetcher.obtenerClima("18.7357,-70.1627")
        ));
        regionesOriginales.add(new PaisRegion("Puerto Rico", new Polygon(
            new int[]{336, 336, 342, 343},
            new int[]{353, 357, 357, 354},
            4),
            ClimaFetcher.obtenerClima("18.2208,-66.5901")
        ));
        regionesOriginales.add(new PaisRegion("Bahamas", new Polygon(
            new int[]{299, 304, 315, 308},
            new int[]{326, 337, 337, 314},
            4),
            ClimaFetcher.obtenerClima("25.0343,-77.3963")
        ));

        regionesOriginales.add(new PaisRegion("Chile", new Polygon(
            new int[]{325, 332, 351, 364, 363, 360, 353, 345, 343, 334, 337, 331, 338, 338, 335, 327},
            new int[]{502, 639, 660, 662, 653, 648, 647, 640, 618, 578, 565, 547, 522, 518, 518, 499},
            16),
            ClimaFetcher.obtenerClima("-35.6751,-71.5430")
        ));

        regionesOriginales.add(new PaisRegion("Albania", new Polygon(
            new int[]{637, 638, 640, 641, 639},
            new int[]{264, 274, 274, 269, 263}, 5),
            ClimaFetcher.obtenerClima("41.1533,20.1683")));

        //Alemania
        regionesOriginales.add(new PaisRegion("Alemania", new Polygon(
        new int[]{596,613,619,611,616,603,597,593,596},
        new int[]{212,208,223,228,236,241,233,221,220},9),
        ClimaFetcher.obtenerClima("51.1657,10.4515")));

        //Austria
        regionesOriginales.add(new PaisRegion("Austria", new Polygon(
        new int[]{606,616,616,627,627,616,611,606},
        new int[]{239,237,234,237,240,246,242,243},8),
        ClimaFetcher.obtenerClima("47.5162,14.5501")));

        //Bélgica
        regionesOriginales.add(new PaisRegion("Belgica", new Polygon(
        new int[]{583,593,593,584},
        new int[]{226,232,227,222},4),
        ClimaFetcher.obtenerClima("50.5039,4.4699")));



        //Bosnia y Herzegovina
        regionesOriginales.add(new PaisRegion("Bosnia y Herzegovina", new Polygon(
        new int[]{625,633,637,633},
        new int[]{252,251,254,261},4),
        ClimaFetcher.obtenerClima("43.9159,17.6791")));

        //Bulgaria
        regionesOriginales.add(new PaisRegion("Bulgaria", new Polygon(
        new int[]{646,648,657,663,663,656,649},
        new int[]{255,266,267,263,256,257,257},7),
        ClimaFetcher.obtenerClima("42.7339,25.4858")));

        //Checo
        regionesOriginales.add(new PaisRegion("Checo", new Polygon(
        new int[]{611,619,631,626,617},
        new int[]{228,225,231,236,233},5),
        ClimaFetcher.obtenerClima("49.8038,15.4749")));

        //Chipre
        regionesOriginales.add(new PaisRegion("Chipre", new Polygon(
        new int[]{680,683,686,687,683,679},
        new int[]{292,292,290,291,296,295},6),
        ClimaFetcher.obtenerClima("35.1264,33.4299")));

        //Croacia
        regionesOriginales.add(new PaisRegion("Croacia", new Polygon(
        new int[]{632,625,621,617,617,622,622,632,624,624,633},
        new int[]{247,246,250,250,253,254,259,262,253,250,250},11),
        ClimaFetcher.obtenerClima("45.1,15.2")));

        //Dinamarca
        regionesOriginales.add(new PaisRegion("Dinamarca", new Polygon(
        new int[]{602,597,601,609},
        new int[]{192,197,207,204},4),
        ClimaFetcher.obtenerClima("56.2639,9.5018")));

        //Eslovaquia
        regionesOriginales.add(new PaisRegion("Eslovaquia", new Polygon(
        new int[]{631,643,645,627,626},
        new int[]{231,231,235,238,235},5),
        ClimaFetcher.obtenerClima("48.6690,19.6990")));

        //Eslovenia
        regionesOriginales.add(new PaisRegion("Eslovenia", new Polygon(
        new int[]{616,625,625,617},
        new int[]{245,241,246,249},4),
        ClimaFetcher.obtenerClima("46.1512,14.9955")));



        //Estonia
        regionesOriginales.add(new PaisRegion("Estonia", new Polygon(
        new int[]{654,642,644,654},
        new int[]{181,184,189,190},4),
        ClimaFetcher.obtenerClima("58.5953,25.0136")));

        //Escocia
        regionesOriginales.add(new PaisRegion("Escocia", new Polygon(
        new int[]{563,568,567,562,563,557,554,557,558},
        new int[]{207,205,193,193,187,190,200,200,206},9),
        ClimaFetcher.obtenerClima("56.4907,-4.2026")));

        //Finlandia
        regionesOriginales.add(new PaisRegion("Finlandia", new Polygon(
        new int[]{648,663,657,640,633,644,637,629,641},
        new int[]{124,164,173,180,166,151,135,129,133},9),
        ClimaFetcher.obtenerClima("61.9241,25.7482")));

        //Francia
        regionesOriginales.add(new PaisRegion("Francia", new Polygon(
        new int[]{583,567,571,559,574,580,597,591,598},
        new int[]{262,257,246,236,232,225,234,245,256},9),
        ClimaFetcher.obtenerClima("46.2276,2.2137")));

        //Grecia
        regionesOriginales.add(new PaisRegion("Grecia", new Polygon(
        new int[]{658,648,640,648,643,649,652,646,659},
        new int[]{266,267,276,280,282,288,281,273,270},9),
        ClimaFetcher.obtenerClima("39.0742,21.8243")));

        //Gales
        regionesOriginales.add(new PaisRegion("Gales", new Polygon(
        new int[]{563,561,556,559},
        new int[]{214,223,222,214},4),
        ClimaFetcher.obtenerClima("52.1307,-3.7837")));

        //Holanda
        regionesOriginales.add(new PaisRegion("Holanda", new Polygon(
        new int[]{594,584,591,595},
        new int[]{212,221,225,219},4),
        ClimaFetcher.obtenerClima("52.1326,5.2913")));

        //Hungría
        regionesOriginales.add(new PaisRegion("Hungria", new Polygon(
        new int[]{645,639,627,625,635,646},
        new int[]{235,238,239,246,247,239},6),
        ClimaFetcher.obtenerClima("47.1625,19.5033")));

        //Inglaterra
        regionesOriginales.add(new PaisRegion("Inglaterra", new Polygon(
        new int[]{563,562,553,556,566,572,576,573,568},
        new int[]{207,223,229,231,226,227,224,214,205},9),
        ClimaFetcher.obtenerClima("52.3555,-1.1743")));
        // Burquina Faso
        regionesOriginales.add(new PaisRegion("burquina", new Polygon(
            new int[]{568, 555, 553, 555, 562, 562, 570, 575}, 
            new int[]{568, 555, 553, 555, 562, 562, 570, 575}, 8),
            ClimaFetcher.obtenerClima("12.2383,-1.5616")
        ));

        // Benín
        regionesOriginales.add(new PaisRegion("benin", new Polygon(
            new int[]{586, 583, 582, 579, 578}, 
            new int[]{391, 395, 403, 403, 390}, 5),
            ClimaFetcher.obtenerClima("9.3077,2.3158")
        ));

        // Costa de Marfil
        regionesOriginales.add(new PaisRegion("costa marfil", new Polygon(
            new int[]{543, 544, 540, 545, 545, 550, 556, 564}, 
            new int[]{388, 395, 400, 404, 412, 408, 407, 409}, 8),
            ClimaFetcher.obtenerClima("7.5400,-5.5471")
        ));

        // Liberia
        regionesOriginales.add(new PaisRegion("liberia", new Polygon(
            new int[]{535, 530, 543, 543}, 
            new int[]{397, 403, 411, 405}, 4),
            ClimaFetcher.obtenerClima("6.4281,-9.4295")
        ));

        // Senegal
        regionesOriginales.add(new PaisRegion("senegal", new Polygon(
            new int[]{516, 511, 520, 533}, 
            new int[]{381, 371, 365, 379}, 4),
            ClimaFetcher.obtenerClima("14.4974,-14.4524")
        ));

        // Egipto
        regionesOriginales.add(new PaisRegion("egipto", new Polygon(
            new int[]{659,659,691,694,694,682,681,684,687,690,685,680,677,673}, 
            new int[]{307,346,345,341,334,320,313,313,321,320,307,308,305,308}, 14),
            ClimaFetcher.obtenerClima("26.8206,30.8025")
        ));

        // Sudán
        regionesOriginales.add(new PaisRegion("sudan", new Polygon(
            new int[]{696,692,660,659,656,656,652,651,655,660,665,668,676,679,683,690,694,701,702,707}, 
            new int[]{341,347,345,354,354,370,373,380,387,390,386,389,389,387,389,380,389,373,364,357}, 20),
            ClimaFetcher.obtenerClima("12.8628,30.2176")
        ));

        // Chad
        regionesOriginales.add(new PaisRegion("chad", new Polygon(
            new int[]{656,628,627,630,628,623,619,622,626,626,624,630,639,648,657,651,651,657}, 
            new int[]{354,340,343,350,364,369,376,374,379,390,393,400,397,385,386,380,373,368}, 18),
            ClimaFetcher.obtenerClima("15.4542,18.7322")
        ));

        // Sudán del Sur
        regionesOriginales.add(new PaisRegion("sudanSur", new Polygon(
            new int[]{688,683,679,675,667,663,656,670,680,684,694,701,695,690,689,694}, 
            new int[]{379,389,386,391,390,386,392,406,409,412,412,407,397,399,392,390}, 16),
            ClimaFetcher.obtenerClima("6.8769,31.3069")
        ));

        // Eritrea
        regionesOriginales.add(new PaisRegion("eritrea", new Polygon(
            new int[]{708,702,702,711,720,722,710}, 
            new int[]{358,363,370,370,380,375,367}, 7),
            ClimaFetcher.obtenerClima("15.1794,39.7823")
        ));

        // Etiopía
        regionesOriginales.add(new PaisRegion("etiopia", new Polygon(
            new int[]{712,703,694,690,691,695,701,711,730,743,727,726,720,720}, 
            new int[]{373,373,392,393,400,398,406,412,408,396,389,385,384,380}, 14),
            ClimaFetcher.obtenerClima("9.145,40.4897")
        ));

        // República Centroafricana
        regionesOriginales.add(new PaisRegion("republicaCentroAfricana", new Polygon(
            new int[]{670,656,658,649,640,640,632,625,630,633,638,642,650,656}, 
            new int[]{408,393,389,386,394,400,398,408,416,413,413,407,413,408}, 14),
            ClimaFetcher.obtenerClima("6.6111,20.9394")
        ));

        // Camerún
        regionesOriginales.add(new PaisRegion("camerun", new Polygon(
            new int[]{623,626,626,623,628,624,631,630,607,607,603,604,608,615}, 
            new int[]{380,380,389,392,399,406,416,420,419,412,409,404,401,401}, 14),
            ClimaFetcher.obtenerClima("7.3697,12.3547")
        ));

        // Somalia
        regionesOriginales.add(new PaisRegion("somalia", new Polygon(
            new int[]{726,736,743,732,722,717,718,722,749,754,747}, 
            new int[]{386,395,395,409,412,417,429,431,403,382,383}, 11),
            ClimaFetcher.obtenerClima("5.1521,46.1996")
        ));
        
        //España
regionesOriginales.add(new PaisRegion("Espana", new Polygon(
    new int[]{545,582,573,568,549,550,544},
    new int[]{258,263,274,286,287,267,266},7),
    ClimaFetcher.obtenerClima("Spain")
));

// Bielorusia
regionesOriginales.add(new PaisRegion("Bielorusia", new Polygon(
new int[]{645,650,654,654,667,673,668,657,647},
new int[]{209,210,204,199,201,209,219,219,220},9),
ClimaFetcher.obtenerClima("Belarus")));






        setLayout(null);

        // Zoom +
        zoomInBtn = new JButton("+");
        zoomInBtn.setBounds(10, 10, 50, 30);
        zoomInBtn.addActionListener(e -> {
            if (escala < ESCALA_MAX) {
                escala *= 1.2;
                if (escala > ESCALA_MAX) escala = ESCALA_MAX;
                limitarOffset();
                repaint();
            }
        });
        add(zoomInBtn);

        // Zoom -
        zoomOutBtn = new JButton("-");
        zoomOutBtn.setBounds(70, 10, 50, 30);
        zoomOutBtn.addActionListener(e -> {
            if (escala > ESCALA_MIN) {
                escala /= 1.2;
                if (escala < ESCALA_MIN) escala = ESCALA_MIN;
                limitarOffset();
                repaint();
            }
        });
        add(zoomOutBtn);

        // Botón "Volver"
        if (conBotonVolver) {
            volverBtn = new JButton("Volver al menú");
            volverBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            volverBtn.setBounds(130, 10, 150, 30);
            volverBtn.setBackground(new Color(255, 255, 255, 220));
            volverBtn.setFocusPainted(false);
            volverBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            volverBtn.addActionListener(e -> {
                SwingUtilities.getWindowAncestor(this).dispose();
                SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
            });

            add(volverBtn);
        }

        addMouseMotionListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);
    }

    private void limitarOffset() {
        int panelW = getWidth();
        int panelH = getHeight();
        int mapaW = (int)(mapa.getWidth() * escala);
        int mapaH = (int)(mapa.getHeight() * escala);

        if (offsetX < 0) offsetX = 0;
        if (offsetX > mapaW - panelW) offsetX = Math.max(mapaW - panelW, 0);

        if (offsetY < 0) offsetY = 0;
        if (offsetY > mapaH - panelH) offsetY = Math.max(mapaH - panelH, 0);
    }

    private Polygon escalarPoligono(Polygon p) {
        int[] xs = new int[p.npoints];
        int[] ys = new int[p.npoints];
        for (int i = 0; i < p.npoints; i++) {
            xs[i] = (int) (p.xpoints[i] * escala - offsetX);
            ys[i] = (int) (p.ypoints[i] * escala - offsetY);
        }
        return new Polygon(xs, ys, p.npoints);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (mapa != null) {
            int w = (int)(mapa.getWidth() * escala);
            int h = (int)(mapa.getHeight() * escala);
            g2.drawImage(mapa, (int)-offsetX, (int)-offsetY, w, h, this);
        }

        regiones.clear();
        for (PaisRegion original : regionesOriginales) {
            Polygon escalado = escalarPoligono(original.getRegion());
            ClimaInfo clima = original.getClima();
            String nombre = original.getNombre();
            regiones.add(new PaisRegion(nombre, escalado, clima));

            g2.setColor(new Color(0, 100, 255, 80));
            g2.fillPolygon(escalado);
            g2.setColor(new Color(0, 100, 255));
            g2.drawPolygon(escalado);
        }

        if (hoverRegion != null) {
            Polygon r = hoverRegion.getRegion();
            Rectangle bounds = r.getBounds();
            int tooltipX = bounds.x + bounds.width / 2 - 110;
            int tooltipY = bounds.y - 95;
            if (tooltipY < 10) tooltipY = bounds.y + bounds.height + 10;

            g2.setColor(new Color(0, 0, 0, 170));
            g2.fillRoundRect(tooltipX, tooltipY, 220, 85, 10, 10);

            g2.setColor(Color.WHITE);
            g2.drawString("País: " + hoverRegion.getNombre(), tooltipX + 10, tooltipY + 20);
            g2.drawString("Clima: " + hoverRegion.getClima().getDescripcion(), tooltipX + 10, tooltipY + 35);
            g2.drawString("Temperatura: " + hoverRegion.getClima().getTemperatura() + " °C", tooltipX + 10, tooltipY + 50);
            g2.drawString("Precipitación: " + hoverRegion.getClima().getPrecipitacion() + " mm", tooltipX + 10, tooltipY + 65);
        }

        // Husos horarios
        g2.setColor(new Color(255, 0, 0, 100));
        double gradosPorHuso = 15.0;
        double pixelesPorGrado = (mapa.getWidth() * escala) / 360.0;
        double centroX = (mapa.getWidth() * escala) / 2.0;

        for (int huso = -12; huso <= 12; huso++) {
            int x = (int)(centroX + huso * gradosPorHuso * pixelesPorGrado - offsetX);
            g2.drawLine(x, 0, x, getHeight());
        }
    }

    @Override public void mouseMoved(MouseEvent e) {
        Optional<PaisRegion> match = regiones.stream()
            .filter(r -> r.getRegion().contains(e.getPoint()))
            .findFirst();
        hoverRegion = match.orElse(null);
        repaint();
    }

    @Override public void mousePressed(MouseEvent e) { dragStart = e.getPoint(); }

    @Override public void mouseDragged(MouseEvent e) {
        if (dragStart != null && escala > ESCALA_MIN) {
            int dx = e.getX() - dragStart.x;
            int dy = e.getY() - dragStart.y;
            offsetX -= dx;
            offsetY -= dy;
            limitarOffset();
            dragStart = e.getPoint();
            repaint();
        }
    }

    @Override public void mouseReleased(MouseEvent e) { dragStart = null; }
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) { hoverRegion = null; repaint(); }

    @Override public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (notches < 0 && escala < ESCALA_MAX) {
            escala *= 1.1;
            if (escala > ESCALA_MAX) escala = ESCALA_MAX;
        } else if (notches > 0 && escala > ESCALA_MIN) {
            escala /= 1.1;
            if (escala < ESCALA_MIN) escala = ESCALA_MIN;
        }
        limitarOffset();
        repaint();
    }
    
    public static void main(String[] args) {
        MapaPanel mapaPrueba = new MapaPanel(true);
        JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           
            frame.setSize(1200, 784);
           frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.add(mapaPrueba);
            frame.setVisible(true);
    }
}
