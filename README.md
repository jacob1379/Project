# 게시판(Simple Bulletin Board) - 개인 프로젝트

## 목적
- 신입 웹 개발자의 기본소양
- 어떤 웹이던지 게시판은 필수요소
- 반복된 연습으로 빠르고 최적화된 게시판 생성을 목표로함

----------------------------------------------------------------------------

## 개발환경
> Front-End
- Bootstrap 5
- Jquery 3
- Thymeleaf

> Back-End
- Spring boot 3.0.4
- JDK 17.0.3
- Oracle Database 11g Express Edition
- Mybatis 3.0.0
- Tomcat 9.0.64
- Maven 4.0.0

----------------------------------------------------------------------------

## 제작기간 - 2023/03/13 ~ 2023/04/02 ( 작업 기간: 3주 )

> 1주차 (03/13 ~ 03/19), View 페이지 및 DB 모델링
- 개발환경 설정 및 프로젝트 주제 설정  
- Bootsrap5를 이용해 HTML View 페이지 설계 및 생성  
- Oracle DB Model 설계 및 생성  
- View 페이지 완성  

> 2주차 (03/20 ~ 03/26), spring MVC 디자인 패턴 적용 및 게시판 기본 기능 완성     
- MVC 디자인 패턴을 고려 한 프로젝트 패키지 설계 및 view 페이지 thymeleaf로 변환  
- 회원가입 기능 구현
- 로그인 및 게시판 view 기능 및 페이징 구현  
- 게시판 쓰기, 수정, 삭제 구현  
- 회원가입 아이디 중복체크, 이메일 인증 구현  

> 3주차 (03/27 ~ 04/02), 댓글, 검색, 추천, 조회수 등 세부 기능 구현 및 마무리  
- 게시판 CRUD 및 페이징 기능을 재사용 하여 댓글 CRUD 구현   
- 추천 및 조회수 기능 구현
- 검색 기능 구현
- 구현된 기능들 보완 및 코드 재정리  
- 마무리

----------------------------------------------------------------------------
## jboard 패키지 구조
![](md_resources/jboardPackage.jpg)

----------------------------------------------------------------------------
## jboard 데이터베이스 모델링
![](md_resources/jboard_model.jpg)

----------------------------------------------------------------------------

## Front-End 기능

> bootstrap5로 View 페이지 설계 및 생성

- 회원가입 페이지 아이디, 이메일 패턴 검사
~~~c
// html
<div>
	<label for="username">아이디</label>
	<span id="username_msg" class="fail"></span>
	<div class="mb-3">
	<input type="text" class="form-control" id="username" name="username">
	</div>
</div>
<div>
	<label for="email">이메일</label>
	<span id="email_msg" class="fail"></span>
	<div class="mb-3">
	<input type="text" class="form-control" id="email" name="email">
	</div>
</div>

// js
function usernameCheck() {
	$("#username_msg").text("");
	const pattern = /^[0-9A-Z]{8,10}$/;
	const $username = $("#username").val().toUpperCase();
	$("#username").val($username);
	const result = pattern.test($username);
	if(result==false)
		$("#username_msg").text("아이디는 대문자와 숫자 8~10자입니다").attr("class", "fail")
	return result;
}

function emailCheck() {
	$("#email_msg").text("");
	const pattern = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
	const $email = $("#email").val();
	const result = pattern.test($email);
	if(result==false)
		$("#email_msg").text("이메일을 정확하게 입력하세요").attr("class", "fail");
	return result;
}

~~~

- 회원가입  알림창
~~~c
// html
<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script> // 알림창 CDN

// js
function join() {
	const formData = new FormData($("#join_form")[0]);
	
	$.ajax({
		url: "/member/new",
		method: "post",
		data: formData,
		processData: false,
		contentType: false
	}).then(
		(result)=>{
			Swal.fire("가입신청 완료","이메일을 확인하세요", "success").then((choice)=>{
				if(choice.isConfirmed) location.href= result.url;
			})
		}, (result)=>{
			Swal.fire("가입신청 실패", msg,"error").then((choice)=>{
				if(choice.isConfirmed) location.href= result.url;
			})
		}
	)
}
~~~

- 게시판 글쓰기, 글수정 Summernote 사용  
~~~c
// html
<div>내용 <textarea id="content" name="content"></textarea></div>

// js
<script>
	// 써머노트 설정
	$('#content').summernote({
		height: 400,           // 에디터 높이
		minHeight: null,       // 최소 높이
		maxHeight: null,       // 최대 높이
		focus: true,           // 에디터 로딩후 포커스를 맞출지 여부
		lang: "ko-KR", 		// 한글 설정
		placeholder: '내용을 입력해주세요.'	//placeholder 설정
	})
</script>
~~~

- 목록 페이지 데이터 뿌리기  
~~~c
// html
<template id="tr_template">
	<tr>
		<td>{bno}</td>
		<td><a href="/board/read?bno={bno}" style="text-decoration: none;">{title}</a></td>
		<td>{writer}</td>
		<td>{writeTime}</td>
		<td>{readCount}</td>
		<td>{commentCount}</td>
	</tr>
</template>

// js
function printBoardList(boardList) {
	const $list = $('#list');
	const $t = $('#tr_template').html();
	for(const b of boardList) {
		$list.append($t.replace('{bno}',b.bno).replace('{bno}',b.bno).replace('{title}',b.title).replace('{writer}',b.writer)
			.replace('{writeTime}', b.writeTime).replace('{readCount}',b.readCount).replace('{commentCount}',b.commentCount));
}

$(document).ready(async function () {
	const apiUrl = 'http://localhost:8087/board/all';
	const pageno = getPageno();
	const params = {pageno};
	$.ajax({url: apiUrl, data: params}).done((response)=>{	
		printBoardList(response.result.boardList);	//ajax통신으로 매핑된 controller의 리스트 데이터를 가져와서 뿌린다
		const pagination = getPagination(response.result);
		printPagination(pagination);
	}).fail((error)=>{
		console.log(error);
	});
})
~~~

----------------------------------------------------------------------------

## Back-End 기능

> 간략 

- 스프링 시큐리티 로그인
~~~c
// Security 설정
@Bean
protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {		
	http.csrf().disable().cors().disable()
	.authorizeHttpRequests(request -> request
        	.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
        	.requestMatchers("/", "/img/**","/member/join","/member/new","/member/login","/css/**","/board/list","/board/all",
        			"/board/read","/board","/js/**","/member/find/**","/member/check/**").permitAll() // 해당 요청URL 허용
                .anyRequest().authenticated()	// 어떠한 요청이라도 인증필요
        )
        .formLogin(login -> login	// form 방식 로그인 사용
        		.loginPage("/member/login")		// 커스텀 로그인 페이지 지정
        		.loginProcessingUrl("/member/login")	// submit받을 URL
        		.usernameParameter("username")
        		.passwordParameter("password")
        		.successHandler(memberLoginSuccessHandler)	// 로그인 성공 핸들러
        		.failureHandler(memberLoginFailureHandler)	// 로그인 실패 핸들러
                	.permitAll()	// 대시보드 이동이 막히면 안되므로 허용
        )
        .logout().logoutUrl("/member/logout").logoutSuccessUrl("/").invalidateHttpSession(true);  // 로그아웃은 기본설정으로 (/logout으로 인증해제)		
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
		return http.build();
}
~~~
~~~c
// UserDetails
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account implements UserDetails{
	private String username;
	private String password;
	private boolean isEnabled;
	private String authorities;
	private MemberDto.User user;
	
	public Account(MemberDto.User user) {
	    Collection<GrantedAuthority> collect = new ArrayList<>();	
	    this.username = user.getUsername();
	    this.password = user.getPassword();
	    this.isEnabled = user.getEnabled();
	    this.authorities = user.getRole();
	    this.user = user;
	}
}

// UserDetailsService
@Service
public class MyUserDetailsService implements UserDetailsService{
	@Autowired
	private MemberDao memberDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Member> memberOptional = memberDao.findById(username);
		Member member = memberOptional.orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
		Account account = new Account(member.toDto()); 
		return account;		// 최종적으로 UserDetails 객체를 리턴한다
	}
}
~~~

- Mail 전송
~~~c
// application.properties에 메일 설정
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=secret@gmail.com
spring.mail.password=sdlkfsdlkfj
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.starttls.enable=true

// Mail Dto
public class Mail {
	private String from;
	private String to;
	private String subject;
	private String text;
}

// Mail 전송
@Component //개발자가 직접 작성한 Class를 Bean으로 등록하기 위한 Annotation
public class MailUtil {
	@Autowired
	private JavaMailSender javaMailSender;
	
	// 메일 설정
	private void sendMail(Mail mail) {
		MimeMessage message = javaMailSender.createMimeMessage(); //MultiPart 데이터를 처리할 수 있고, 메일의 내용을 설정
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, false, "utf-8"); // false는 멀티파트 메세지를 사용하지 않겠다는 의미
			helper.setFrom(mail.getFrom());
			// application.properties에 설정한 아이디는 단순히 smtp 인증을 받기 위해 사용함 따라서 보낸이(setFrom())는 반드시 필요
			helper.setTo(mail.getTo());
			helper.setSubject(mail.getSubject());
			helper.setText(mail.getText(), true); // true는 html을 사용하겠다는 의미
			javaMailSender.send(message);
		} catch(MessagingException e) {
			e.printStackTrace();
		}
	}
}
~~~

- 게시판 페이징   
~~~c
// Dto
@Data
@AllArgsConstructor
public static class Page {
	private Integer pageno;		// 페이지 번호
	private Integer pagesize;	// 한 페이지의 사이즈
	private Integer totalCount;	// 전체 게시글 수
	private Collection<ListDto> boardList;	// 게시글
}

// Service
public BoardDto.Page list(Integer pageno, String writer) {
	Integer totalCount = dao.count(writer);
	Integer countOfPage = (totalCount-1)/pagesize + 1;	// 전체 페이지 수
	if(pageno>countOfPage) {	
		pageno = countOfPage;
	}else if(pageno<0) {
		pageno = -pageno;
	}else if(pageno==0) {
		pageno = 1;
	}
	Integer start = (pageno-1)*pagesize+1;	// 게시글 번호에 따라 보여지는 페이지의 시작 번호
	Integer end = start+pagesize-1;		// 게시글 번호에 따라 보여지는 페이지의 마지막 번호
	
	Map<String, Object> map = new HashMap<>();
	map.put("start", start);
	map.put("end", end);
	map.put("writer", writer);
	return new Page(pageno, pagesize, totalCount, dao.findAll(map));	
}
~~~

----------------------------------------------------------------------------

## 요청 처리 과정
`요청 > 컨트롤러 > 서비스 > 서비스 구현 > DAO > Mapper > Mybatis > DB > 컨트롤러(DB 반환)`

>  Member  
  
- 아이디 중복 확인
~~~c
// 중복 확인 요청
$("#username").on("blur", function() {
	if(usernameCheck()==false)
		return false;
	$.ajax("/member/check/username?username=" + $("#username").val())
		.then(
			()=>$("#username_msg").text("좋은 아이디네요").attr("class", "success"),
			()=>$("#username_msg").text("사용중인 아이디입니다").attr("class", "fail")
		);
});
~~~
~~~c
// Controller
// Ajax 요청이기 때문에 화면 이동없이 요청 URL Body에 문자열을 반환해준다
@Controller
public class MemberController {
	@Autowired
	private MemberService service;
	
	@GetMapping(path="/member/check/username", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> idCheck(@Valid MemberDto.Idcheck dto) {
		if(service.idCheck(dto))
			return ResponseEntity.ok(new RestResponse("OK", "사용 가능한 아이디입니다", null));
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new RestResponse("Fail", "사용중인 아이디입니다", null));
	}
}
~~~
~~~c
// Service
@Service
public class MemberService {
	@Autowired
	private MemberDao memberDao;
	
	// 아이디 중복 확인
	public Boolean idCheck(MemberDto.Idcheck dto) {
		return !memberDao.existsById(dto.getUsername());
	}
}
~~~
~~~c
// Dao
@Mapper
public interface MemberDao {
	public Boolean existsById(String username);
}
~~~
~~~c
// Mapper(.xml)
<mapper namespace="com.example.demo.dao.MemberDao">
	<select id="existsById" resultType="boolean">
		select count(*) from member where username=#{username} and rownum&lt;=1
	</select>
<mapper>
~~~

## 회원가입    
![](md_resources/signUp.gif)    
## 로그인  
![](md_resources/로그인.gif)       
## 내 정보 조회   
![](md_resources/latestLogin.gif)  
## 로그아웃  
![](md_resources/recommendTime.gif)
## 아이디 찾기  
![](md_resources/recommendTime.gif)
## 비밀번호 찾기
![](md_resources/recommendTime.gif)
## 비밀번호 변경
![](md_resources/recommendTime.gif)
## 회원탈퇴
![](md_resources/recommendTime.gif)

----------------------------------------------------------------------------

> Board 
## 전체 게시글 가져오기 및 페이징   
![](md_resources/boardList.gif)   
## 비로그인 게시글 읽기,쓰기  
![](md_resources/boardRead.gif)    
## 조회 수 올리기  
![](md_resources/countHit.gif)
## 추천 수 올리기
![](md_resources/recommendTime.gif)
## 게시글 쓰기 
![](md_resources/boardWrite.gif)  
## 게시글 수정  
![](md_resources/boardUpdate.gif)  
## 게시글 삭제  
![](md_resources/boardDelete.gif)  

----------------------------------------------------------------------------

> Comment   
## 댓글 작성    
![](md_resources/replyWrite.gif)  
## 댓글 삭제  
![](md_resources/replyDelete.gif)    
## 댓글 수 세기    
![](md_resources/replyUpdate.gif) 

----------------------------------------------------------------------------

## 보완점
- 파일 업로드 / 다운로드 추가  
- 쿠기 로그인 추가(자동 로그인)     
- 네이버 로그인 연동 추가  
- 댓글, 추천 Ajax로 구현   

## 프로젝트 중 실수 List
- 프로젝트에 mysql8 적용 문제 >> 낮은 버전의 jdk 7 이하 tomcat 8 이하는 쓸수 없었음
- 프로젝트에 커스텀 config 적용 문제 >> web.xml 에 커스텀 config 경로 지정하여 해결
- 프로젝트 jdk 버전 변경 문제 >> project - properties - project Facets 에서 변경
- 정적 리소스들 스프링 적용 문제 >> webapp > resouces 폴더에 넣음으로써 해결
- 프로젝트 run on server 시, 프로젝트명(1.0.0 snapshot) 으로 저장되던 문제
	- pom.xml 에 build 태그 하위에 finalname 태그 적용으로 해결
	- ex) <build><finalname>프로젝트명</finalname>...다른 옵션들</build>
- java class getters/setters 인식 문제
	- 자동으로 오버로드 되는 이름명을 쓰지 않으면 인식을 못함
	- 커스텀으로 변경했던 이름명 지우고 자동생성으로 해결
- js console.log 작동확인 문제 >> 브라우저 콘솔창에서 확인 (IDE에서 확인했음)
- js에서 jq submit(); 무반응 >> form 속성 action으로 submit 경로를 지정하지 않았기 때문
- 컨트롤러 @RequestMapping 된 메서드 String 문자열 return 시, URL 경로 반환 문제
	- 반환 타입앞에 @ResponseBody를 붙임으로써 view로 데이터 반환이 가능해짐
- js에서 ajax > controller 로 한글 파라미터 전송 시, 한글 깨짐 현상 
	- Tomcat7 - sever.xml - <Connector URIEncoding="UTF-8"> 추가
- java에서 controller > view(.jsp) 로 한글 데이터 반환 시, ???으로 전송되는 현상
	-  @RequestMapping(속성 produces = "application/text; charset=utf8") 추가
- js에서 sq submit(); 무반응 >> form 속성 method 방식 지정 필요
- 컨트롤러에서 커맨드 객체 사용시, view에서 오는 파라미터 못받는 문제
	- 커맨드 객체 변수명들과 파라미터명들이 일치해야 받을 수 있다
- 컨트롤러 로그인 세션 생성시에 무한로딩 문제
	- UserVO 변수와 user_info에 실제로 정의된 테이블 컬럼이 같지 않아 오류 발생
- jstl c:choose 조건문 안에 주석이 들어가면 오류 발생
- 같은 값이지만 다른 주소를 참조하는 비교를 할때 "=="연산자를 써서 비교하는 실수
	- java에서는 실제 값을 비교할때는 equals() 메소드를 쓴다
	- "=="는 참조 주소값을 비교하는 연산자
