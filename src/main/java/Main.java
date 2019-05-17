import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        /**
         *
         * ССЫЛКИ
         *
          */

        final String linksTXT = ".\\links.txt";
        final String albumLinksTXT = ".\\albumLinks.txt";

        /**
         *
         * ССЫЛКИ
         *
         */

        ArrayList<String> linksList = new ArrayList<>();

        FileOutputStream writer = new FileOutputStream(linksTXT);
        writer.write(("").getBytes());
        writer.close();
                //возможно, не стоит парсить альбомы каждый раз
        //TODO защиту от пустых строк или что там может еще случиться
        //TODO ОЧИСТКУ ФАЙЛА ССЫЛОК ПРИ ЗАПУСКЕ, И ПРОВЕРКУ, СУЩЕСТВУЕТ ЛИ ОН
            //получаем список ссылок на песни со страниц альбомов
        getByAlbum(albumLinksTXT);

            //заносим ссылки в массив
        linksList=readFile(linksTXT);
//        try{
//            FileInputStream songLinks = new FileInputStream("E:\\Programs\\Intellij IdEA Ultimate\\ProjectsFolder\\webPageParser\\links.txt");
//            BufferedReader br = new BufferedReader(new InputStreamReader(songLinks));
//            String strLine;
//            while ((strLine = br.readLine()) != null){
//             //   System.out.println(strLine);
//                linksList.add(strLine);
//            }
//        }catch (IOException e){
//            System.out.println("Ошибка" + e);
//        }
            //парсим наши ссылки
        parseLink(linksList);



//        linksList.forEach(link ->{
//            String url = link;
//            Document document = null;
//            try {
//                document = Jsoup.connect(url).userAgent("Chrome/4.0.249.0 Safari/532.5")
//                        .referrer("http://www.google.com").get();
//            } catch (IOException e) {
//                System.out.println("Ошибка" + e);
//            }
//
//            String songName = document.select("h1.header_with_cover_art-primary_info-title").text();
//            System.out.println("Song name: " + songName);
//            String artist = document.select("a.header_with_cover_art-primary_info-primary_artist").text();
//            System.out.println("Artist: " + artist);
//
//            Elements bars = document.select("div.lyrics");
//
//            //  bars.select("br").after("\\n");
//            for (Element line : bars) {
//                String[] tempArray;
//                line.select("br").after("3845909eee2144qwe");
//
//                String delimiter = "3845909eee2144qwe";
//                String bar=line.text();
//                tempArray=bar.split(delimiter);
//                System.out.println("BARS: " + line.text());
//                for (int i=0; i<tempArray.length; i++){
//                    System.out.println(tempArray[i]);
//                }
//
//                try(BufferedWriter bw = new BufferedWriter(new FileWriter( artist + " - " + songName + " lyrics.txt")))
//                {
//                    for (int i=0; i<tempArray.length; i++){
//                        System.out.println(tempArray[i]);
//                        bw.write(tempArray[i]+"\n\t");
//                    }
//
//
//                }
//                catch(IOException ex){
//
//                    System.out.println(ex.getMessage());
//                }
//            }
//            });

    }

    static void getByAlbum(String link){
        ArrayList<String> albumLinksList;
        albumLinksList=readFile(link);
        albumLinksList.forEach(linkk ->{
            System.err.println(linkk);
        });

        albumLinksList.forEach(albumLink -> {

            Document document = null;
            try {                                              //типа обход антиспама
                document = Jsoup.connect(albumLink).userAgent("Chrome/4.0.249.0 Safari/532.5")
                        .referrer("http://www.google.com").get();//источник запроса
            } catch (IOException e) {
                System.out.println("Ошибка!" + e);
            }


            Elements albumHref = null;
            if (document != null) {
                albumHref = document.select("a.u-display_block");
            }

            try(BufferedWriter bw = new BufferedWriter(new FileWriter( "links.txt", true)))
                {
                    if (albumHref != null) {
                        for (Element element : albumHref) {
                            String relHref = element.attr("href");
                            bw.append(relHref).append("\n\t");

                    }
                    }

                }
                catch(IOException ex){

                    System.out.println(ex.getMessage());
                }


        });
        //удаляем последнюю строку, потому что там постоянно остается \n\t

        try {
            RandomAccessFile f = new RandomAccessFile("links.txt", "rw");
            long length = f.length() - 1;
            byte b=0;
            do {
                length -= 1;
                f.seek(length);
               b = f.readByte();
            } while(b != 10);
            f.setLength(length+1);
            f.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


private static void parseLink(ArrayList<String> list){
    list.forEach(link ->{
        String url = link;
        Document document = null;
        try {
            document = Jsoup.connect(url).userAgent("Chrome/4.0.249.0 Safari/532.5")
                    .referrer("http://www.google.com").get();
        } catch (IOException e) {
            System.out.println("Ошибка" + e);
        }

        String songName = null;
        if (document != null) {
            songName = document.select("h1.header_with_cover_art-primary_info-title").text();
            songName = songName.replaceAll("[\\ / ? : * \" > < |]", " ");
        }
        System.out.println("Song name: " + songName);
        String artist = null;
        if (document != null) {
            artist = document.select("a.header_with_cover_art-primary_info-primary_artist").text();
            artist = artist.replaceAll("[\\ / ? : * \" > < |]", " ");
        }
        System.out.println("Artist: " + artist);
        String album = null;
        if (document != null) {
            album = document.select("a.song_album-info-title").text();
            album = album.replaceAll("[\\ / ? : * \" > < |]", " ");
        }
        System.out.println("Artist: " + artist);

        Elements bars = null;
        if (document != null) {
            bars = document.select("div.lyrics");
        }

        //  bars.select("br").after("\\n");
        if (bars != null) {
            for (Element line : bars) {
                String[] tempArray;
                line.select("br").after("3845909eee2144qwe");

                String delimiter = "3845909eee2144qwe";
                String bar = line.text();
                tempArray = bar.split(delimiter);
                System.out.println("BARS: " + line.text());
                for (int i = 0; i < tempArray.length; i++) {
                    System.out.println(tempArray[i]);
                }

                File directory = new File(".\\" + artist + "\\" + album + "\\");
                directory.mkdirs();
                if (directory.exists() && directory.isDirectory()) {
                 writeFile(tempArray, artist, album, songName);

                } else {
                    directory = new File(".\\" + artist + "\\" + album + "\\");
                    if(directory.mkdirs()) {
                        System.out.println("Папка создана!");
                        writeFile(tempArray, artist, album, songName);

                    } else {
                        writeFile(tempArray, artist, album, songName);
                        System.out.println("Папка не создана!");
                    }


                }
            }
        }
    });
}


 private static ArrayList<String> readFile(String link){
     ArrayList<String> list = new ArrayList<>();
     try{
         FileInputStream albumLinks = new FileInputStream(link);
         BufferedReader br = new BufferedReader(new InputStreamReader(albumLinks));
         String strLine;
         while ((strLine = br.readLine()) != null){

                 list.add(strLine);

         }
        } catch (IOException e){
         System.out.println("Ошибка " + e);
     }
return list;
 }

 private static void writeFile(String[] tempArray, String artist, String album, String songName){
     try (BufferedWriter bw = new BufferedWriter(new FileWriter(".\\" + artist + "\\" + album + "\\" + artist + " - " + songName + " lyrics.txt"))) {
         for (int i = 0; i < tempArray.length; i++) {
             System.out.println(tempArray[i]);
             bw.write(tempArray[i] + "\n\t");
         }

     } catch (IOException ex) {

         System.out.println(ex.getMessage());
     }
 }

}
