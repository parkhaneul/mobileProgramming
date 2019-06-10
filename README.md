# DayGram

Native android diary application made with Android studio and Kotlin

[Google Slide](https://docs.google.com/presentation/d/1JV8ZpxUkhAn4ATgANTKQ7OQGjDD8qCmuUl9mMc4-3HI/edit?usp=sharing)


# 주요 기능

- 글 작성
  
        카메라 버튼을 눌러 사진을 찍으면 글 작성 Activity로 이동한다
        
        제목과 내용을 입력 후 저장하면 SQLite DB에 추가한다
        
        메인 화면에서 DB와 RecyclerView를 동기화한다

        
- 글 확인
    
        카드를 터치하여 글의 내용을 볼 수 있다
        
        휴지통 버튼으로 글을 삭제할 수 있다
        
        사진을 찍을 때 저장된 위치 정보를 볼 수 있다
        
        인터넷 연결이 되어 있는 경우 위치 정보를 주소로 보여 준다
        
        
- 검색
    
        메인 화면 위의 검색 바에서 저장된 글 중 검색을 할 수 있다
        
        검색창 아래의 연도를 터치해 특정한 연도에 작성된 글만 모아 볼 수 있다
