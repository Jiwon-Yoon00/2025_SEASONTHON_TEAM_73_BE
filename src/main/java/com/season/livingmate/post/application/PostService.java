package com.season.livingmate.post.application;

import com.season.livingmate.exception.CustomException;
import com.season.livingmate.exception.Response;
import com.season.livingmate.exception.status.ErrorStatus;
import com.season.livingmate.exception.status.SuccessStatus;
import com.season.livingmate.geo.api.dto.GeoResult;
import com.season.livingmate.geo.api.dto.KakaoAddressRes;
import com.season.livingmate.geo.application.GeoService;
import com.season.livingmate.geo.support.RegionLabelSeoul;
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
import com.season.livingmate.user.domain.Gender;
import com.season.livingmate.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final GeoService geoService;

    // 게시글 생성
    @Transactional
    public Response<Long> createPost(PostCreateReq req, MultipartFile imageFile, User user) {

        // 이미지 파일 처리
        String imageUrl = null;
        if(imageFile != null && !imageFile.isEmpty()){
            imageUrl = uploadImage(imageFile);
        }

        // 주소, 좌표 확정
        Double lat = req.latitude();
        Double lng = req.longitude();
        String address = req.location();

        // 주소만 존재할 시 좌표로 변환
        if((lat == null || lng == null) && address != null && !address.isBlank()){
            GeoResult g = geoService.geocodeOne(address)
                    .orElseThrow(() -> new CustomException(ErrorStatus.BAD_REQUEST, "주소를 좌표로 변환할 수 없습니다."));
            lat = g.lat();
            lng = g.lng();
            address = g.address(); // 도로명 주소로 덮어씀
        }

        // 필수값 검증
        if(address == null || address.isBlank() || lat == null || lng == null){
            throw new CustomException(ErrorStatus.BAD_REQUEST, "location/latitude/longitude가 필요합니다.");
        }

        // 서울시 구 동 라벨 생성
        KakaoAddressRes.Document doc = geoService.findFirstDocument(address).orElse(null);
        String regionLabel = (doc != null)
                ? RegionLabelSeoul.toSeoulGuDong(doc)                           // 주소검색 성공
                : geoService.regionByCoord(lat, lng)                             // 주소검색 실패 → 좌표 폴백
                .map(GeoService.RegionParts::regionLabel)
                .orElse("");
        if (regionLabel.isBlank()) {
            throw new CustomException(ErrorStatus.BAD_REQUEST, "현재는 서울 지역만 지원합니다.");
        }

        GeoPoint geo = new GeoPoint(lat, lng);

        PaymentStructure payment = new PaymentStructure(
                req.depositShare(),
                req.rentShare(),
                req.maintenanceShare(),
                req.utilitiesShare()
        );

        String preferredGenderString = req.preferredGender().stream()
                .map(Gender::name)
                .collect(Collectors.joining(","));

        Post post = Post.builder()
                .title(req.title())
                .content(req.content())
                .imageUrl(imageUrl)
                .geoPoint(geo)
                .location(address)
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
                .preferredGender(preferredGenderString)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        post.applyRegionLabel(regionLabel); // 시 구 동

        Long id = postRepository.save(post).getPostId();
        return Response.success(SuccessStatus.CREATE_POST, id);
    }

    // 이미지 업로드 메서드
    private String uploadImage(MultipartFile file) {
        try {
            // 파일 타입 검증
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new CustomException(ErrorStatus.BAD_REQUEST, "이미지 파일만 업로드 가능합니다.");
            }

            // 파일 크기 검증 (5mb 제한)
            if (file.getSize() > 5 * 1024 * 1024) {
                throw new CustomException(ErrorStatus.BAD_REQUEST, "이미지 파일 크기는 5MB 이하여야 합니다.");
            }

            // 업로드 디렉토리 생성
            String uploadDir = "uploads/images/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일명 생성
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;

            // 파일 저장
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            // URL 반환
            return "https://livingmate.store/images/" + filename;

        } catch (IOException e) {
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }
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
    public Response<Long> updatePost(Long postId, PostUpdateReq req, MultipartFile imageFile, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_POST));

        if(!post.getUser().getId().equals(currentUser.getId())){
            throw new CustomException(ErrorStatus.FORBIDDEN);
        }

        // 이미지 파일 처리
        String imageUrl = post.getImageUrl(); // 기존 이미지 URL 유지
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = uploadImage(imageFile);
        }

        Double lat = req.latitude();
        Double lng = req.longitude();
        String address = req.location();

        if ((lat == null || lng == null) && address != null && !address.isBlank()){
            GeoResult g = geoService.geocodeOne(address)
                    .orElseThrow(() -> new CustomException(ErrorStatus.BAD_REQUEST, "주소를 좌표로 변환할 수 없습니다."));
            lat = g.lat();
            lng = g.lng();
            address = g.address(); // 도로명 주소로 덮어씀
        }

        GeoPoint geo = (lat != null && lng != null) ? new GeoPoint(lat, lng) : post.getGeoPoint();

        // 주소 또는 좌표가 바뀌었다면 라벨 재계산
        if ((lat != null && lng != null) || (address != null && !address.isBlank())) {
            String newRegion = geoService.regionByCoord(geo.getLatitude(), geo.getLongitude())
                    .map(GeoService.RegionParts::regionLabel)
                    .orElse("");

            if (newRegion.isBlank()) {
                throw new CustomException(ErrorStatus.BAD_REQUEST, "현재는 서울 지역만 지원합니다.");
            }
            post.applyRegionLabel(newRegion);
        }

        PaymentStructure payment = new PaymentStructure(
                req.depositShare(),
                req.rentShare(),
                req.maintenanceShare(),
                req.utilitiesShare()
        );

        String preferredGenderString = req.preferredGender().stream()
                .map(Gender::name)
                .collect(Collectors.joining(","));

        post.update(
                req.title(),
                req.content(),
                imageUrl,
                geo,
                (address != null && !address.isBlank()) ? address : post.getLocation(),
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
                req.roomCount(),
                preferredGenderString
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
