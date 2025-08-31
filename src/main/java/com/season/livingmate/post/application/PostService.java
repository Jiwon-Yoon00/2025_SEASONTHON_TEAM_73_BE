package com.season.livingmate.post.application;

import com.season.livingmate.exception.CustomException;
import com.season.livingmate.exception.Response;
import com.season.livingmate.exception.status.ErrorStatus;
import com.season.livingmate.exception.status.SuccessStatus;
import com.season.livingmate.post.api.dto.req.PostCreateReq;
import com.season.livingmate.post.api.dto.req.PostSearchReq;
import com.season.livingmate.post.api.dto.req.PostUpdateReq;
import com.season.livingmate.post.api.dto.res.PostDetailRes;
import com.season.livingmate.post.api.dto.res.PostListRes;
import com.season.livingmate.post.domain.GeoPoint;
import com.season.livingmate.post.domain.PaymentStructure;
import com.season.livingmate.post.domain.Post;
import com.season.livingmate.post.domain.repository.PostRepository;
import com.season.livingmate.post.domain.repository.PostSpecs;
import com.season.livingmate.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    // 게시글 생성
    @Transactional
    public Response<Long> createPost(PostCreateReq req, User user) {

        GeoPoint geo = new GeoPoint(req.latitude(), req.longitude());
        PaymentStructure payment = new PaymentStructure(
                req.depositShare(),
                req.rentShare(),
                req.maintenanceShare(),
                req.utilitiesShare()
        );

        Post post = Post.builder()
                .title(req.title())
                .content(req.content())
                .imageUrl(req.imageUrl())
                .geoPoint(geo)
                .location(req.location())
                .roomType(req.roomType())
                .deposit(req.deposit())
                .monthlyRent(req.monthlyRent())
                .maintenanceFee(req.maintenanceFee())
                .paymentStructure(payment)
                .floor(req.floor())
                .buildingFloor(req.buildingFloor())
                .areaSize(req.areaSize())
                .heatingType(req.heatingType())
                .hasElevator(req.hasElevator())
                .availableDate(req.availableDate())
                .minStayMonths(req.minStayMonths())
                .maxStayMonths(req.maxStayMonths())
                .washroomCount(req.washroomCount())
                .roomCount(req.roomCount())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .build();

        Long id = postRepository.save(post).getPostId();
        return Response.success(SuccessStatus.CREATE_POST, id);
    }

    // 게시글 단건 조회
    public Response<PostDetailRes> getDetail(Long postId){
        Post p = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_POST));

        return Response.success(SuccessStatus.GET_POST, PostDetailRes.from(p));
    }

    // 게시글 목록 조회
    public Response<Page<PostListRes>> getList(Pageable pageable){
        Page<PostListRes> page =  postRepository.findAll(pageable)
                .map(PostListRes::from);
        return Response.success(SuccessStatus.GET_POST_LIST, page);
    }

    // 게시글 수정
    @Transactional
    public Response<Long> updatePost(Long postId, PostUpdateReq req, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_POST));

        if(!post.getUser().getId().equals(currentUser.getId())){
            throw new CustomException(ErrorStatus.FORBIDDEN);
        }

        GeoPoint geo = new GeoPoint(req.latitude(), req.longitude());
        PaymentStructure payment = new PaymentStructure(
                req.depositShare(),
                req.rentShare(),
                req.maintenanceShare(),
                req.utilitiesShare()
        );

        post.update(
                req.title(),
                req.content(),
                req.imageUrl(),
                geo,
                req.location(),
                req.roomType(),
                req.deposit(),
                req.monthlyRent(),
                req.maintenanceFee(),
                payment,
                req.floor(),
                req.buildingFloor(),
                req.areaSize(),
                req.heatingType(),
                req.hasElevator(),
                req.availableDate(),
                req.minStayMonths(),
                req.maxStayMonths(),
                req.washroomCount(),
                req.roomCount()
        );

        return Response.success(SuccessStatus.UPDATE_POST, post.getPostId());
    }

    // 게시글 삭제
    @Transactional
    public Response<Long> deletePost(Long postId, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_POST));

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new CustomException(ErrorStatus.FORBIDDEN);
        }

        postRepository.delete(post);
        return Response.success(SuccessStatus.DELETE_POST, postId);
    }

    // 게시글 검색
    @Transactional(readOnly = true)
    public Response<Page<PostListRes>> searchPost(PostSearchReq req, Pageable pageable){
        // 입력된 값이 있으면 AND로, 없으면 전체 조회
        Specification<Post> spec = PostSpecs.build(req);

        Page<PostListRes> page = postRepository.findAll(spec, pageable)
                .map(PostListRes::from);

        return Response.success(SuccessStatus.GET_POST_LIST, page);
    }
}
