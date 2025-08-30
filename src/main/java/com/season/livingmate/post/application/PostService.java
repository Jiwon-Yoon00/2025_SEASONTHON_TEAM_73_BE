package com.season.livingmate.post.application;

import com.season.livingmate.exception.CustomException;
import com.season.livingmate.exception.Response;
import com.season.livingmate.exception.status.ErrorStatus;
import com.season.livingmate.exception.status.SuccessStatus;
import com.season.livingmate.post.api.dto.req.PostCreateReq;
import com.season.livingmate.post.api.dto.req.PostUpdateReq;
import com.season.livingmate.post.api.dto.res.PostDetailRes;
import com.season.livingmate.post.api.dto.res.PostListRes;
import com.season.livingmate.post.domain.GeoPoint;
import com.season.livingmate.post.domain.PaymentStructure;
import com.season.livingmate.post.domain.Post;
import com.season.livingmate.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Response<Long> createPost(PostCreateReq req){

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
                .build();

        Long id = postRepository.save(post).getPostId();
        return Response.success(SuccessStatus.CREATE_POST, id);
    }

    // 게시글 단건 조회
    public Response<PostDetailRes> getDetail(Long postId){
        Post p = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_POST));

        PostDetailRes dto =  new PostDetailRes(
                p.getPostId(),
                p.getTitle(),
                p.getContent(),
                p.getImageUrl(),
                p.getGeoPoint() != null ? p.getGeoPoint().getLatitude() : null,
                p.getGeoPoint() != null ? p.getGeoPoint().getLongitude() : null,
                p.getLocation(),
                p.getRoomType(),
                p.getDeposit(),
                p.getMonthlyRent(),
                p.getMaintenanceFee(),
                p.getPaymentStructure() != null && p.getPaymentStructure().isDepositShare(),
                p.getPaymentStructure() != null && p.getPaymentStructure().isRentShare(),
                p.getPaymentStructure() != null && p.getPaymentStructure().isMaintenanceShare(),
                p.getPaymentStructure() != null && p.getPaymentStructure().isUtilitiesShare(),
                p.getFloor(),
                p.getBuildingFloor(),
                p.getAreaSize(),
                p.getHeatingType(),
                p.isHasElevator(),
                p.getAvailableDate(),
                p.getMinStayMonths(),
                p.getMaxStayMonths(),
                p.getWashroomCount(),
                p.getRoomCount(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );

        return Response.success(SuccessStatus.GET_POST, dto);
    }

    // 게시글 목록 조회
    public Response<Page<PostListRes>> getList(Pageable pageable){
        Page<PostListRes> page =  postRepository.findAll(pageable)
                .map(p -> new PostListRes(
                        p.getPostId(),
                        p.getTitle(),
                        p.getAvailableDate()
                ));
        return Response.success(SuccessStatus.GET_POST_LIST, page);
    }

    // 게시글 수정
    @Transactional
    public Response<Long> updatePost(Long postId, PostUpdateReq req){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_POST));

        return Response.success(SuccessStatus.UPDATE_POST, post.getPostId());
    }

    // 게시글 삭제
    @Transactional
    public Response<Long> deletePost(Long postId){
        Post p = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_POST));
        postRepository.delete(p);
        return Response.success(SuccessStatus.DELETE_POST, postId);
    }
}
