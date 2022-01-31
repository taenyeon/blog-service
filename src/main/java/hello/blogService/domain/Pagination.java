package hello.blogService.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Pagination {

    private int listSize = 10;                //초기값으로 목록개수를 10으로 셋팅
    private int rangeSize = 10;            //초기값으로 페이지범위를 10으로 셋팅
    private int page;
    private int range;
    private int listCnt;
    private int pageCnt;
    private int startPage;
    private int startList;
    private int endList;
    private int endPage;
    private boolean prev;
    private boolean next;
    private String searchType; // 검색 기능 -> 종류
    private String keyword; // 검색 기능 -> 키워드

    public int getStartList() {
        return startList;
    }

    public void pageInfo(int page, int range, int listCnt) {
        this.page = page;
        this.range = range;
        this.listCnt = listCnt;

        //전체 페이지수
        this.pageCnt = (int) Math.ceil((float)listCnt / listSize);
        System.out.println(this.pageCnt);

        //시작 페이지
        this.startPage = (range - 1) * rangeSize + 1;
        System.out.println(this.startPage);
        //끝 페이지
        this.endPage = range * rangeSize;
        System.out.println(this.endPage);
        //게시판 시작번호
        this.startList = (page - 1) * listSize;
        System.out.println(this.startList);

        this.endList = page * listSize;

        //이전 버튼 상태
        this.prev = range != 1;

        //다음 버튼 상태
        this.next = endPage <= pageCnt;
        if (this.endPage > this.pageCnt) {
            this.endPage = this.pageCnt;
        }

    }
}