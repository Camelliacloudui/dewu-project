package com.xcx.dewu.control;

import com.xcx.dewu.service.ProductDetailService;
import com.xcx.dewu.model.Paging;
import com.xcx.dewu.model.Product;
import com.xcx.dewu.model.ProductDetail;
import com.xcx.dewu.model.Result;
import com.xcx.dewu.param.BasePageParam;
import com.xcx.dewu.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/controller/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDetailService productDetailService;

    @GetMapping("/products")
    @ResponseBody
    public Result getProductList(@RequestParam(defaultValue = "1") int pageNum,
                                 @RequestParam(defaultValue = "10") int pageSize) {
        // 1. 构建分页参数
        BasePageParam param = new BasePageParam();
        param.setPagination(pageNum);
        param.setPageSize(pageSize);

        // 2. 调用 ProductService 查询商品主表
        Paging<Product> paging = productService.pageQueryProduct(param);

        Result result = new Result();
        result.setSuccess(true);
        result.setData(paging);
        // 3. 返回结果
        return result;
    }

    @GetMapping("/product/{productId}")
    @ResponseBody
    public Result getProductDetail(@PathVariable("productId") String productId) {
        // 1. 查询商品主信息 (价格、标题等)
        Product product = productService.get(productId);

        // 2. 查询商品详细信息 (图文描述等)
        List<ProductDetail> details = productDetailService.getByProductId(productId);

        // 3. 将两部分数据组合成一个新对象返回给前端
        Map<String, Object> detailVO = new HashMap<>();
        detailVO.put("product", product);
        detailVO.put("details", details);

        Result result = new Result();
        result.setSuccess(true);
        result.setData(detailVO);

        return result;
    }
}
