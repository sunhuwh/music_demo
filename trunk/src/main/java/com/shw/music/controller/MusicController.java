package com.shw.music.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Strings;
import com.google.gson.Gson;

@Controller
public class MusicController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/")
	public String index(HttpServletRequest request, Model model){
		HttpSession session = request.getSession();
		String word = session.getAttribute("word") != null ? 
				String.valueOf(session.getAttribute("word")) : null;
		model.addAttribute("word", word);
		return "index";
	}
	
	@GetMapping("/musics")
	@ResponseBody
	public List musics(@RequestParam("word")String word, HttpServletRequest request){
		HttpSession session = request.getSession();
		session.setAttribute("word", word);
		return findMusicByBaiDu(word);
	}
	
	public String getUrlById(@PathVariable String id){
		HttpHeaders headers = new HttpHeaders();  
        headers.set("auth_token", "asdfgh");  
        headers.set("Other-Header", "othervalue");  
        headers.setContentType(MediaType.parseMediaTypes("application/javascript").get(0));  
		HttpEntity<Map> entity = new HttpEntity<Map>(null, headers);
		String a = restTemplate.getForEntity("http://music.baidu.com/data/music/fmlink?songIds="+id+"&type=flac", String.class).getBody();
		Map musicDetail = (new Gson()).fromJson(a, Map.class);
		System.out.println("musicDetail:"+musicDetail);
		Map musicData = (Map) musicDetail.get("data");
		List<Map> songList = (List) musicData.get("songList");
		if(songList!=null&&!songList.isEmpty()){
			for (Map song : songList) {
				String link = String.valueOf(song.get("songLink"));
				return link;
			}
		}
		return null;
	}

	private List findMusicByBaiDu(String word) {
		Map result =restTemplate.getForObject("http://sug.music.baidu.com/info/suggestion?format=json&word={word}&version=2&from=0&third_type=0&client_type=0&_={time}", Map.class, word, System.currentTimeMillis());
		if(result.get("errno")!=null){
			return null;
		}
		Map data = (Map)result.get("data");
		if(data.get("song")==null||((List)data.get("song")).isEmpty()){
			return null;
		}
		List<Map> songs = (List)data.get("song");
		return songs.stream().filter(song->{
			String id = String.valueOf(song.get("songid"));
			String url = getUrlById(id);
			song.put("downUrl", url);
			return !Strings.isNullOrEmpty(url);
		}).collect(Collectors.toList());
	}
	
}
