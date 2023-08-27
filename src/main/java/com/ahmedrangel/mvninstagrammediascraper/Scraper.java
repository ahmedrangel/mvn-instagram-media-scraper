package com.ahmedrangel.mvninstagrammediascraper;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.Arrays;

public class Scraper {
  public static void scraper() {
    // Instgram post or reel URL example
    String url = "https://www.instagram.com/reel/CtjoC2BNsB2/?igshid=MzRlODBiNWFlZA==";

    // Required headers example
    String _userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36";
    String _cookie = "mid=...; ig_did=... datr=...; fbm_...; ds_user_id=...; csrftoken=...; fbsr_=... rur=\"...\"";
    String _xIgAppId = "93661974...";
    String idUrl = getId(url);
    if (idUrl == null) {
      System.out.println("Invalid URL");
    } else {
      try {
        // Fetch data from instagram post
        String fetchUrl = "https://www.instagram.com/p/" + idUrl + "?__a=1&__d=di";
        HttpResponse<String> response = Unirest.get(fetchUrl)
        .header("cookie", _cookie)
        .header("user-agent", _userAgent)
        .header("x-ig-app-id", _xIgAppId)
        .header("sec-fetch-site", "same-origin")
        .asString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(response.getBody()).get("items").get(0);

        // Get items from instagram post
        String
        code = node.get("code") != null ? node.get("code").asText() : null,
        username = node.get("user").get("username") != null ? node.get("user").get("username").asText() : null,
        full_name = node.get("user").get("full_name") != null ? node.get("user").get("full_name").asText() : null,
        profile_picture = node.get("user").get("profile_pic_url") != null ? node.get("user").get("profile_pic_url").asText() : null,
        product_type = node.get("product_type") != null ? node.get("product_type").asText() : null,
        caption = node.get("caption") != null ? node.get("caption").get("text").asText() : null,
        location = node.get("location") != null ? node.get("location").get("text").asText() : null,
        image_url = node.get("image_versions2") != null ? node.get("image_versions2").get("candidates").get(0).get("url").asText() : null,
        video_url = node.get("video_versions") != null ? node.get("video_versions").get(0).get("url").asText() : null;

        Integer
        created_at = node.get("taken_at") != null ? node.get("taken_at").asInt() : null,
        like_count = node.get("like_count") != null ? node.get("like_count").asInt() : null,
        comment_count = node.get("comment_count") != null ? node.get("comment_count").asInt() : null,
        height = node.get("original_height") != null ? node.get("original_height").asInt() : null,
        width = node.get("original_width") != null ? node.get("original_width").asInt() : null,
        view_count = null;
        if (node.has("view_count")) {
            view_count = node.get("view_count").asInt();
        } else if (node.has("play_count")) {
            view_count = node.get("play_count").asInt();
        }

        Double
        video_duration = node.get("video_duration") != null ? node.get("video_duration").asDouble() : null;

        Boolean
        is_verified = node.get("user").get("is_verified") != null ? node.get("user").get("is_verified").asBoolean() : null,
        is_paid_partnership = node.get("is_paid_partnership") != null ? node.get("is_paid_partnership").asBoolean() : null;
        
        String[] carousel_images = null, carousel_videos = null;

        // Check if post is a carousel
        if (node.get("product_type").asText().equals("carousel_container")) {
          JsonNode carousel_container = node.get("carousel_media");
          int size = carousel_container.size(); 
          carousel_images = new String[size];
          carousel_videos = new String[size];
          for (int i = 0; i < size; i++) {
            carousel_images[i] = carousel_container.get(i).get("image_versions2") != null ? carousel_container.get(i).get("image_versions2").get("candidates").get(0).get("url").asText() : null;
            carousel_videos[i] = carousel_container.get(i).get("video_versions") != null ? carousel_container.get(i).get("video_versions").get(0).get("url").asText() : null;
          }
        }
      
        // Print all data
        System.out.println( "Code: " + code + "\n" +
                            "Username: " + username + "\n" +
                            "Full Name: " + full_name + "\n" +
                            "Profile Picture: " + profile_picture + "\n" +
                            "Product Type: " + product_type + "\n" +
                            "Caption: " + caption + "\n" +
                            "Location: " + location + "\n" +
                            "Image URL: " + image_url + "\n" +
                            "Video URL: " + video_url + "\n" +
                            "Created At: " + created_at + "\n" +
                            "Like Count: " + like_count + "\n" +
                            "Comment Count: " + comment_count + "\n" +
                            "View Count: " + view_count + "\n" +
                            "Height: " + height + "\n" +
                            "Width: " + width + "\n" +
                            "Video Duration: " + video_duration + "\n" +
                            "Is Verified: " + is_verified + "\n" +
                            "Is Paid Partnership: " + is_paid_partnership + "\n" +
                            "Carousel Images: " + Arrays.toString(carousel_images) + "\n" +
                            "Carousel Videos: " + Arrays.toString(carousel_videos) + "\n"
                          );
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  // Function to get instagram post ID from any url string
  public static String getId(String url) {
    Pattern pattern = Pattern.compile("\\/([a-zA-Z0-9_-]+)(?:\\.[a-zA-Z0-9]+)?(?:\\?|$|\\/\\?|\\/$)");
    Matcher matcher = pattern.matcher(url);
    return matcher.find() && matcher.groupCount() > 0 ? matcher.group(1) :null;
  }
}
